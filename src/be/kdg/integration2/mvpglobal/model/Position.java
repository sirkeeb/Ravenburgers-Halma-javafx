package be.kdg.integration2.mvpglobal.model;

/**
 * Represents a coordinate on the Halma board.
 * <p>
 * Encapsulates a row and column index. Two {@code Position}
 * instances are equal if and only if both their row and column values match.
 * </p>
 */
public class Position {
    private int row;
    private int col;

    /**
     * Constructs a Position with the specified row and column.
     *
     * @param row the row index
     * @param col the column index
     */
    public Position(int row, int col) {
        this.row = row;
        this.col = col;
    }

    /**
     * Returns the row index of this position.
     *
     * @return the row
     */
    public int getRow() {
        return row;
    }

    /**
     * Returns the column index of this position.
     *
     * @return the column
     */
    public int getCol() {
        return col;
    }

    /**
     * Compares this position to another position
     * <p>
     * Two positions are equal if the other object is a {@code Position}
     * with identical row and column values.
     * </p>
     *
     * @param o the object to compare with
     * @return {@code true} if {@code o} is a Position with the same row and column; {@code false} otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Position p = (Position) o;
        return this.row == p.row && this.col == p.col;
    }

    /**
     * Returns a string representation of this position.
     *
     * @return a string in the format "(row,col)"
     */
    @Override
    public String toString() {
        return "(" + row + "," + col + ")";
    }
}
