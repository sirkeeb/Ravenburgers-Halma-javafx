package be.kdg.integration2.mvpglobal.model;

/**
 * Abstract base class for a game participant in Halma.
 * <p>
 * Defines the player’s display name, piece color, and the contract
 * for obtaining a move given the current board state.
 * Subclasses must implement the {@link #getMove(Board)} method
 * to provide human input or AI logic.
 * </p>
 */
public abstract class Player {
    protected String name;
    protected PieceColor color;

    /**
     * Initializes a player with the specified name and piece color.
     *
     * @param name  the display name of the player
     * @param color the color of pieces this player will control
     */
    public Player(String name, PieceColor color) {
        this.name = name;
        this.color = color;
    }

    /**
     * Returns the display name of this player.
     *
     * @return the player’s name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the color of pieces this player controls.
     *
     * @return the player’s {@link PieceColor}
     */
    public PieceColor getColor() {
        return color;
    }

    /**
     * Determines and returns the next move for this player.
     * <p>
     * Implementations should examine the provided {@code board}
     * and return a valid {@link Move}, or {@code null} if no move
     * can be made.
     * </p>
     *
     * @param board the current game board to evaluate
     * @return the chosen {@code Move}, or {@code null} if none available
     */
    public abstract Move getMove(Board board);
}
