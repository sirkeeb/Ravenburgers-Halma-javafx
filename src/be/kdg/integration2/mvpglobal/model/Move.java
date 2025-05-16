package be.kdg.integration2.mvpglobal.model;

/**
 * Represents a move of a piece from one board position to another.
 * <p>
 * Encapsulates the starting and ending {@link Position} of a single step or jump.
 * </p>
 */
public class Move {
    private Position start;
    private Position end;

    /**
     * Constructs a Move with the specified start and end positions.
     *
     * @param start the original {@link Position} of the piece
     * @param end   the destination {@link Position} of the piece
     */
    public Move(Position start, Position end) {
        this.start = start;
        this.end = end;
    }

    public Position getStart() { return start; }
    public Position getEnd()   { return end; }

    /**
     * Returns a human-readable description of this move.
     *
     * @return a String in the form "Move from &lt;start&gt; to &lt;end&gt;"
     */
    @Override
    public String toString() {
        return "Move from " + start + " to " + end;
    }
}
