package be.kdg.integration2.mvpglobal.view.gamescreen;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules.RulesHandler;
import be.kdg.integration2.mvpglobal.view.leaderboardscreen.LeaderboardPresenter;
import be.kdg.integration2.mvpglobal.view.leaderboardscreen.LeaderboardView;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenView;
import be.kdg.integration2.mvpglobal.view.statsscreen.StatsPresenter;
import be.kdg.integration2.mvpglobal.view.statsscreen.StatsView;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * The {@code GamePresenter} class acts as the controller in the MVP architecture for the game screen.
 * It handles user interactions on the {@link GameView}, manages the {@link Board} model, coordinates moves
 * (including AI logic), and interacts with the database for tracking users and game statistics.
 */
public class GamePresenter {
    private final GameView view;
    private final Board board;
    private final Stage stage;
    private final Player black, white;
    private Player currentPlayer;
    private Position selected;
    private final boolean vsAI;
    private long moveStartTime;

    private final DatabaseConnections db = new DatabaseConnections();
    private int userId = 0, gameId = 0;

    private int moveCount = 0;
    private double totalDurationSec = 0.0;

    /**
     * Constructs the game presenter, initializes the model, view bindings, and player setup.
     *
     * @param view     the visual game interface
     * @param stage    the JavaFX stage to render scenes
     * @param username the current human player's username
     * @param vsAI     true if the opponent is an AI, false if it's a second human player
     */
    public GamePresenter(GameView view, Stage stage, String username, boolean vsAI) {
        this.view = view;
        this.stage = stage;
        this.board = new Board();
        this.vsAI = vsAI;

        this.black = new HumanPlayer(username, PieceColor.BLACK);
        this.white = vsAI
                ? new AI("Computer", PieceColor.WHITE)
                : new HumanPlayer("Player 2", PieceColor.WHITE);

        this.currentPlayer = black;
        this.selected = null;

        attachHandlers();
        refreshBoard();
        if (vsAI) {
            ((AI)white).setRulesHandler(new RulesHandler());
        }
        db.createTables();
        db.insertUser(username, username + "@placeholder.com");
        userId = db.getUsername(username);
        db.insertGame(userId, false);
        gameId = db.getLatestGameId(userId);

        moveStartTime = System.nanoTime();
    }

    /**
     * Attaches event handlers for board interactions and buttons such as Back, Leaderboard, and Force Win.
     */
    private void attachHandlers() {
        for (Node node : view.boardGrid.getChildren()) {
            node.setOnMouseClicked(e -> {
                Integer rr = GridPane.getRowIndex(node);
                Integer cc = GridPane.getColumnIndex(node);
                handleCellClick(rr == null ? 0 : rr, cc == null ? 0 : cc);
            });
        }
        view.winButton.setOnAction(e -> showEndStats(currentPlayer.getColor()));

        view.getBackButton().setOnAction(e -> {
            StartScreenView startScreenView = new StartScreenView();
            StartScreenPresenter presenter = new StartScreenPresenter(startScreenView, stage);

            Scene startScreenScene = new Scene(startScreenView, 800, 640);
            stage.setScene(startScreenScene);
            stage.show();
        });

        view.getLeaderboardButton().setOnAction(e -> {
            LeaderboardView leaderboardView = new LeaderboardView();
            LeaderboardPresenter presenter = new LeaderboardPresenter(leaderboardView, stage);
            Scene leaderboardScene = new Scene(leaderboardView, 800, 640);
            stage.setScene(leaderboardScene);
            stage.show();
        });
    }

    /**
     * Handles the logic when a cell on the board is clicked.
     * Supports selecting a piece, validating and performing moves, and updating the game state.
     *
     * @param row the row of the clicked cell
     * @param col the column of the clicked cell
     */
    private void handleCellClick(int row, int col) {
        Position pos = new Position(row, col);

        if (selected == null) {
            var piece = board.getPieceAt(pos);
            if (piece != null && piece.getColor() == currentPlayer.getColor()) {
                selected = pos;
                view.toggleHighlight(row, col); // highlight selection
            }
            return;
        }

        if (selected.getRow() == row && selected.getCol() == col) {
            selected = null;
            view.clearHighlight(); // unselect
            return;
        }

        if (!board.isLegalMove(selected, pos)) {
            selected = null;
            view.clearHighlight(); // cancel selection
            return;
        }

        board.movePiece(selected, pos);
        long end = System.nanoTime();
        double durationSeconds = (end - moveStartTime) / 1_000_000_000.0;
        double roundedSeconds = Math.round(durationSeconds * 100.0) / 100.0;

        db.insertMove(gameId, roundedSeconds);
        moveCount++;
        totalDurationSec += roundedSeconds;

        selected = null;
        view.clearHighlight(); // clear highlight after move
        switchPlayer();
        refreshBoard();

        if (vsAI && currentPlayer instanceof AI) {
            Move aiMove = currentPlayer.getMove(board);
            if (aiMove != null) {
                board.movePiece(aiMove.getStart(), aiMove.getEnd());
                refreshBoard();

                if (board.hasPlayerWon(currentPlayer.getColor())) {
                    showEndStats(currentPlayer.getColor());
                    return;
                }

                switchPlayer();
                moveStartTime = System.nanoTime();
            }
        } else {
            moveStartTime = System.nanoTime();
        }
        if (selected == null) {
            var piece = board.getPieceAt(pos);
            if (piece != null && piece.getColor() == currentPlayer.getColor()) {
                selected = pos;
                view.toggleHighlight(row, col); // highlight selection
            }
            return;
        }

        if (selected.getRow() == row && selected.getCol() == col) {
            selected = null;
            view.clearHighlight(); // unselect
            return;
        }

        if (!board.isLegalMove(selected, pos)) {
            selected = null;
            view.clearHighlight(); // cancel selection
            return;
        }

        board.movePiece(selected, pos);
        selected = null;
        view.clearHighlight();
    }

    /**
     * Switches the current player to the next player.
     * Also checks if the newly active player has already won the game.
     */
    private void switchPlayer() {
        currentPlayer = (currentPlayer == black) ? white : black;

        if (board.hasPlayerWon(currentPlayer.getColor())) {
            showEndStats(currentPlayer.getColor());
        }
    }

    /**
     * Redraws the entire board view based on the current state of the model.
     * It clears old pieces and draws all current pieces in their positions.
     */
    private void refreshBoard() {
        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Piece piece = board.getPieceAt(new Position(row, col));
                if (piece != null) {
                    view.drawPiece(row, col, piece.getColor() == PieceColor.BLACK ? "black" : "white");
                } else {
                    view.getCellPane(row, col).getChildren().removeIf(n -> n instanceof Circle);
                }
            }
        }
    }

    /**
     * Displays the end-of-game statistics screen.
     * Also updates the database if the human player has won.
     *
     * @param winner the color of the winning player
     */
    private void showEndStats(PieceColor winner) {
        if (winner == PieceColor.WHITE) {
            StatsView statsView = new StatsView();
            StatsPresenter presenter = new StatsPresenter(statsView, stage, userId, false);
            Scene statsScene = new Scene(statsView, 800, 640);
            stage.setScene(statsScene);
            stage.show();
        } else {
            StatsView statsView = new StatsView();
            StatsPresenter presenter = new StatsPresenter(statsView, stage, userId, true);
            Scene statsScene = new Scene(statsView, 800, 640);
            stage.setScene(statsScene);
            stage.show();
        }
        if (winner == PieceColor.BLACK && black instanceof HumanPlayer) {
            db.updateGame(userId, true);
        }
    }
}
