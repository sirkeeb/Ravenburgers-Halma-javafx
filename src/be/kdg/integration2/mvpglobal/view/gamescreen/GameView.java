package be.kdg.integration2.mvpglobal.view.gamescreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

/**
 * The GameView class represents the main visual component of the game.
 * It displays a 16x16 board grid, game control buttons, and provides utility methods
 * to draw game pieces, highlight selected tiles, and manage layout/background.
 */
public class GameView extends BorderPane {
    public final GridPane boardGrid = new GridPane();
    public final Button backButton = new Button("Back to Menu");
    public final Button leaderboardButton = new Button("Leaderboard");
    public final Button winButton = new Button("Force Win");

    private final int TILE_SIZE = 40;
    private final int BOARD_SIZE = 16;

    private StackPane selectedCell = null;
    private final Background highlightBackground = new Background(new BackgroundFill(Color.web("#87CEFA", 0.5), CornerRadii.EMPTY, Insets.EMPTY));
    private final Background normalBackground = null; // Default background (no fill)

    /**
     * Constructs the GameView, initializing the board, layout, and background.
     */
    public GameView() {
        setupBoard();
        setupLayout();
        setupBackground();
    }

    /**
     * Initializes the game board as a 16x16 GridPane with alternating tile colors.
     * Each cell contains a StackPane with a Rectangle background.
     */
    private void setupBoard() {
        boardGrid.setHgap(2);
        boardGrid.setVgap(2);

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane cellPane = new StackPane();
                Rectangle tile = new Rectangle(TILE_SIZE, TILE_SIZE);
                tile.setFill((row + col) % 2 == 0 ? Color.valueOf("#fdf6e3") : Color.valueOf("#b18b6a"));
                tile.setStrokeWidth(2);
                tile.setStroke(Color.TRANSPARENT);

                cellPane.setUserData(tile);
                cellPane.getChildren().add(tile);

                boardGrid.add(cellPane, col, row);
            }
        }
    }

    /**
     * Configures the main layout of the view including the game board and control buttons.
     */
    private void setupLayout() {
        setPrefSize(1200, 900);
        // Wrap boardGrid in a StackPane so it stays centered
        StackPane boardWrapper = new StackPane(boardGrid);
        boardWrapper.setAlignment(Pos.CENTER);
        boardWrapper.setMaxWidth(Double.MAX_VALUE);
        boardWrapper.setMaxHeight(Double.MAX_VALUE);
        HBox.setHgrow(boardWrapper, Priority.ALWAYS);

        // Button panel
        VBox buttonBox = new VBox(20, backButton, leaderboardButton, winButton);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(20));
        buttonBox.setPrefWidth(240); // 20% of 1200
        buttonBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.8);");

        // Main layout container
        HBox mainLayout = new HBox(boardWrapper, buttonBox);
        mainLayout.setPrefWidth(1200);  // Total window width
        mainLayout.setPrefHeight(900);  // Total window height

        setCenter(mainLayout);
    }

    /**
     * Retrieves the StackPane of the cell at the specified row and column.
     *
     * @param row the row index of the cell
     * @param col the column index of the cell
     * @return the StackPane representing the cell, or null if not found
     */
    public StackPane getCellPane(int row, int col) {
        for (Node node : boardGrid.getChildren()) {
            Integer r = GridPane.getRowIndex(node);
            Integer c = GridPane.getColumnIndex(node);
            if ((r == null ? 0 : r) == row && (c == null ? 0 : c) == col) {
                return (StackPane) node;
            }
        }
        return null;
    }

    /**
     * Draws a circular game piece in the specified cell.
     *
     * @param row   the row index
     * @param col   the column index
     * @param color either "black" or any other value interpreted as white
     */
    public void drawPiece(int row, int col, String color) {
        StackPane cell = getCellPane(row, col);
        if (cell == null) return;

        Circle piece = new Circle(TILE_SIZE / 2.5);
        RadialGradient gradient;

        if (color.equalsIgnoreCase("black")) {
            gradient = new RadialGradient(0, 0, 0.3, 0.3, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#cccccc")), new Stop(1, Color.web("#000000")));
        } else {
            gradient = new RadialGradient(0, 0, 0.5, 0.5, 1, true, CycleMethod.NO_CYCLE,
                    new Stop(0, Color.web("#ffffff")), new Stop(0.7, Color.web("#f0f0f0")), new Stop(1, Color.web("#cccccc")));
            piece.setStroke(Color.web("#cccccc"));
            piece.setStrokeWidth(1.5);
        }

        piece.setFill(gradient);
        piece.setEffect(new DropShadow(6.0, 2.0, 2.0, Color.color(0, 0, 0, 0.3)));

        cell.getChildren().removeIf(n -> n instanceof Circle);
        cell.getChildren().add(piece);
    }

    /**
     * Sets a background image for the account creation screen.
     * The image is scaled to cover the entire window area.
     */
    private void setupBackground() {
        Image backgroundImage = new Image("file:resources/images/backgroundimage.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)
        );
        setBackground(new Background(background));
    }

    /**
     * Highlights the specified cell or removes the highlight if it is already selected.
     *
     * @param row the row index
     * @param col the column index
     */
    public void toggleHighlight(int row, int col) {
        StackPane cell = getCellPane(row, col);
        if (cell == null) return;

        Rectangle tile = (Rectangle) cell.getUserData();
        if (tile == null) return;

        // If re-selecting the same tile, unhighlight
        if (cell == selectedCell) {
            tile.setStroke(Color.TRANSPARENT);
            selectedCell = null;
        } else {
            // Unhighlight previous
            if (selectedCell != null) {
                Rectangle prevTile = (Rectangle) selectedCell.getUserData();
                if (prevTile != null) prevTile.setStroke(Color.TRANSPARENT);
            }
            // Highlight new selection
            tile.setStroke(Color.DODGERBLUE);
            selectedCell = cell;
        }
    }

    /**
     * Clears any highlight applied to a selected cell.
     */
    public void clearHighlight() {
        if (selectedCell != null) {
            Rectangle tile = (Rectangle) selectedCell.getUserData();
            if (tile != null) tile.setStroke(Color.TRANSPARENT);
            selectedCell = null;
        }
    }

    /**
     * @return the "Back to Menu" button
     */
    public Button getBackButton() {
        return backButton;
    }

    /**
     * @return the "Leaderboard" button
     */
    public Button getLeaderboardButton() {
        return leaderboardButton;
    }
}
