package be.kdg.integration2.mvpglobal.model;

import java.util.Scanner;

/**
 * Represents a human-controlled player in the Halma game.
 * <p>
 * Prompts the user via the console to enter move coordinates,
 * validates the input, and returns a {@code Move} object or {@code null}
 * if the input is invalid.
 * </p>
 */
public class HumanPlayer extends Player {
    private final Scanner scanner = new Scanner(System.in);

    /**
     * Creates a new human player with the given name and piece color.
     *
     * @param name  the display name of the player
     * @param color the color of pieces this player controls
     */
    public HumanPlayer(String name, PieceColor color) {
        super(name, color);
    }

    /**
     * Prompts the user to enter the starting and ending coordinates for a move,
     * validates the positions and ownership of the piece, and returns the move.
     * <p>
     * Prints error messages and returns {@code null} if:
     * <ul>
     *   <li>Either coordinate pair is off the board</li>
     *   <li>The starting cell is empty or contains an opponent’s piece</li>
     *   <li>The destination cell is not empty</li>
     * </ul>
     * </p>
     *
     * @param board the current game board used for validation
     * @return a {@code Move} from the user’s input, or {@code null} if invalid
     */
    @Override
    public Move getMove(Board board) {
        System.out.print("Enter start row and col: ");
        int startRow = scanner.nextInt();
        int startCol = scanner.nextInt();
        System.out.print("Enter end row and col: ");
        int endRow = scanner.nextInt();
        int endCol = scanner.nextInt();

        Position from = new Position(startRow, startCol);
        Position to   = new Position(endRow, endCol);

        if (!board.isValidPosition(from) || !board.isValidPosition(to)) {
            System.out.println("Invalid position.");
            return null;
        }
        if (board.getPieceAt(from) == null || board.getPieceAt(from).getColor() != color) {
            System.out.println("Invalid piece.");
            return null;
        }
        if (!board.isEmpty(to)) {
            System.out.println("Destination not empty.");
            return null;
        }

        return new Move(from, to);
    }
}
