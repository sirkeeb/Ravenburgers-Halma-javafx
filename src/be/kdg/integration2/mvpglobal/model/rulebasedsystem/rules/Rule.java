package be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;

public abstract class Rule {
    public abstract boolean conditionRule(FactsHandler facts);
    public abstract boolean  actionRule(FactsHandler facts, Board board, Move move);// returns true if the new move was determined, returns false if only the facts have been modified

    /**
     * Clones the given board to simulate a move without affecting the original board.
     *
     * @param original The original board to be cloned.
     * @return A deep copy of the original board.
     */
        protected Board cloneBoard(Board original) {
            Board clone = new Board();

            for (int row = 0; row < Board.SIZE; row++) {
                for (int col = 0; col < Board.SIZE; col++) {
                    Position pos = new Position(row, col);
                    Piece piece = original.getPieceAt(pos);
                    if (piece != null) {
                        clone.setPieceAt(pos, new Piece(piece.getColor()));
                    } else {
                        clone.setPieceAt(pos, null);
                    }
                }
            }

            return clone;
        }
    }


