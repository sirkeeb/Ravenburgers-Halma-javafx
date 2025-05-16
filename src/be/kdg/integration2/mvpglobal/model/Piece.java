package be.kdg.integration2.mvpglobal.model;

/**
 * Represents a single game piece with an associated color.
 * <p>
 * A {@code Piece} holds only its {@link PieceColor}; movement and placement
 * are managed by the {@code Board} and {@code Player} classes.
 * </p>
 */
public class Piece {
    private PieceColor color;

    /**
     * Creates a new Piece of the specified color.
     *
     * @param color the color of this piece (BLACK or WHITE)
     */
    public Piece(PieceColor color) {
        this.color = color;
    }

    /**
     * Returns the color of this piece.
     *
     * @return the {@link PieceColor} of this piece
     */
    public PieceColor getColor() {
        return color;
    }
}
