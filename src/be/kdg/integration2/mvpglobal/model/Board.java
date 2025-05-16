package be.kdg.integration2.mvpglobal.model;

import java.util.*;

/**
 * Models the game board for Halma.
 * <p>
 * Maintains a grid of Positions and Pieces, and provides methods
 * to query, modify, and validate the board state.
 * </p>
 *
 * <p>Key responsibilities:</p>
 * <ul>
 *   <li>Tracking piece placement and removal</li>
 *   <li>Determining legal moves and jumps</li>
 *   <li>Cloning board states for simulation</li>
 * </ul>
 *
 * @author Kacper
 * @version 1.0
 */
public class Board {
    public static final int SIZE = 16;
    private Piece[][] grid;

    private final Set<Position> blackCamp = Set.of(
            new Position(0, 0), new Position(0, 1), new Position(0, 2), new Position(0, 3), new Position(0, 4),
            new Position(1, 0), new Position(1, 1), new Position(1, 2), new Position(1, 3), new Position(1, 4),
            new Position(2, 0), new Position(2, 1), new Position(2, 2), new Position(2, 3),
            new Position(3, 0), new Position(3, 1), new Position(3, 2),
            new Position(4, 0), new Position(4, 1)
    );

    private final Set<Position> whiteCamp = Set.of(
            new Position(15, 15), new Position(15, 14), new Position(15, 13), new Position(15, 12), new Position(15, 11),
            new Position(14, 15), new Position(14, 14), new Position(14, 13), new Position(14, 12), new Position(14, 11),
            new Position(13, 15), new Position(13, 14), new Position(13, 13), new Position(13, 12),
            new Position(12, 15), new Position(12, 14), new Position(12, 13),
            new Position(11, 15), new Position(11, 14)
    );

    public Board() {
        grid = new Piece[SIZE][SIZE];
        initializeBoard();
    }

    public int getSize() {
        return SIZE;
    }

    /**
     * Initializes the game board to its starting configuration.
     * <p>
     * Clears all cells by setting them to {@code null}, then places
     * BLACK pieces in the upper-left triangular camp (rows 0–5),
     * and WHITE pieces in the lower-right triangular camp (rows 10–15).
     * After placement, ensures the two central cells in each camp are empty:
     * (0,5), (5,0), (10,15), and (15,10).
     * </p>
     */
    private void initializeBoard() {
        for (int row = 0; row < SIZE; row++)
            for (int col = 0; col < SIZE; col++)
                grid[row][col] = null;

        for (int row = 0; row <= 5; row++)
            for (int col = 0; col <= 5 - row; col++)
                grid[row][col] = new Piece(PieceColor.BLACK);

        for (int row = 0; row < 16; row++)
            for (int col = 0; col <= 5 - row; col++)
                grid[15 - row][15 - col] = new Piece(PieceColor.WHITE);

        grid[0][5] = null;
        grid[5][0] = null;

        grid[10][15] = null;
        grid[15][10] = null;
    }

    /**
     * Checks if the given position falls within the board’s valid range.
     * <p>
     * A position is valid if its row and column indices are each between 0 (inclusive)
     * and {@code SIZE} (exclusive).
     * </p>
     *
     * @param pos the Position to verify
     * @return {@code true} if {@code pos} is on the board; {@code false} otherwise
     */
    public boolean isValidPosition(Position pos) {
        return pos.getRow() >= 0
                && pos.getRow() < SIZE
                && pos.getCol() >= 0
                && pos.getCol() < SIZE;
    }

    public boolean isEmpty(Position pos) {
        return isValidPosition(pos) && getPieceAt(pos) == null;
    }

    /**
     * Returns the piece at the specified position.
     *
     * @param pos the board coordinate
     * @return the Piece at pos, or null if empty
     */
    public Piece getPieceAt(Position pos) {
        return grid[pos.getRow()][pos.getCol()];
    }

    public void setPieceAt(Position pos, Piece piece) {
        grid[pos.getRow()][pos.getCol()] = piece;
    }

    /**
     * Executes a move on this board, updating piece positions.
     */
    public void movePiece(Position from, Position to) {
        Piece piece = getPieceAt(from);
        setPieceAt(to, piece);
        setPieceAt(from, null);
    }

    public Piece[][] getGrid() {
        return grid;
    }

    /**
     * Returns all adjacent empty positions that a piece can move to in a single step.
     * <p>
     * Scans the eight surrounding cells (up, down, left, right, and diagonals)
     * and collects those that lie within the board bounds and are unoccupied.
     * </p>
     *
     * @param from the current position of the piece
     * @return a list of valid destination positions reachable by a simple move
     */
    public List<Position> getSimpleValidMoves(Position from) {
        List<Position> moves = new ArrayList<>();
        int row = from.getRow();
        int col = from.getCol();
        // Check all 8 directions around the piece
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue; // Skip the original square
                Position to = new Position(row + dr, col + dc);
                if (isValidPosition(to) && isEmpty(to)) {
                    moves.add(to);
                }
            }
        }
        return moves;
    }

    /**
     * Determines whether a move from one position to another is legal.
     * <p>
     * Considers single‐step adjacency, valid jump over a piece,
     * and whether the destination cell is empty.
     * </p>
     *
     * @param from the starting Position of the piece
     * @param to   the target Position to move to
     * @return {@code true} if the move is legal under Halma rules; {@code false} otherwise
     */
    public boolean isLegalMove(Position from, Position to) {
        if (!isValidPosition(from) || !isValidPosition(to)) return false;
        if (!isEmpty(to)) return false;
        int dr = Math.abs(to.getRow() - from.getRow());
        int dc = Math.abs(to.getCol() - from.getCol());
        // single‐step move
        if (dr <= 1 && dc <= 1) return true;
        // jump move (exactly two spaces, with a piece in between)
        if ((dr == 2 || dc == 2) && (dr <= 2 && dc <= 2)) {
            int midRow = (from.getRow() + to.getRow()) / 2;
            int midCol = (from.getCol() + to.getCol()) / 2;
            Position mid = new Position(midRow, midCol);
            return getPieceAt(mid) != null;
        }
        return false;
    }

    /**
     * Checks whether the specified player has filled their target camp.
     * <p>
     * For BLACK, verifies that every cell in the bottom‐right triangular camp
     * (the last four rows) is occupied by a BLACK piece.
     * For WHITE, verifies that every cell in the top‐left triangular camp
     * (the first four rows) is occupied by a WHITE piece.
     * </p>
     *
     * @return {@code true} if all goal‐camp positions are occupied by the given color; {@code false} otherwise
     */

    public boolean hasPlayerWon(PieceColor color) {
        Set<Position> targetCamp = (color == PieceColor.BLACK) ? whiteCamp : blackCamp;
        int correctPieces = 0;

        for (Position pos : targetCamp) {
            Piece p = grid[pos.getRow()][pos.getCol()];
            if (p != null && p.getColor() == color) {
                correctPieces++;
            }
        }

        // A player wins only when all their pieces are in the opponent's starting camp
        return correctPieces == blackCamp.size(); // both camps are assumed to be the same size
    }
}