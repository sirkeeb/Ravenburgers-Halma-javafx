/*package be.kdg.integration2.mvpglobal.model;

public class Halma {
    private final Board board = new Board();
    private final Player black = new HumanPlayer("You", PieceColor.BLACK);
    private final Player white = new AI("Computer", PieceColor.WHITE);
    private Player currentPlayer = black;

    private final DatabaseConnections db = new DatabaseConnections();
    private int userId = 0, gameId = 0;

    public void start() {
        db.createTables();
        db.insertUser(black.getName(), black.getName() + "@example.com");
        userId = db.getUsername(black.getName());
        db.insertGame(userId, false);
        gameId = db.getLatestGameId(userId);

        while (true) {
            printBoard();
            System.out.println("Turn: " + currentPlayer.getName());

            Move move = currentPlayer.getMove(board);
            if (move != null) {
                long startTime = System.nanoTime();
                board.movePiece(move.getStart(), move.getEnd());
                long endTime   = System.nanoTime();
                double rawMs   = (endTime - startTime) / 1_000_000.0;
                double duration = Math.round(rawMs * 100.0) / 100.0;
                db.insertMove(gameId, duration);
            } else {
                System.out.println("No valid move");
            }

            if (board.hasPlayerWon(currentPlayer.getColor())) {
                printBoard();
                System.out.println(currentPlayer.getName() + " wins!");
                db.insertGame(userId, true);
                db.closeConnection();
                break;
            }

            if (currentPlayer == black) {
                currentPlayer = white;
            } else {
                currentPlayer = black;
            }
        }
    }


    private void printBoard() {
        System.out.println();
        for (int r = 0; r < Board.SIZE; r++) {
            for (int c = 0; c < Board.SIZE; c++) {
                Piece p = board.getGrid()[r][c];
                System.out.print(p == null ? ". " :
                        (p.getColor() == PieceColor.BLACK ? "B " : "W "));
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void main(String[] args) {
        new Halma().start();
    }
}
*/