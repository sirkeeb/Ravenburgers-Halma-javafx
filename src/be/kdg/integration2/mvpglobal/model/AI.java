/**
 * Represents the computer-controlled player in the Halma game.
 * <p>
 * Implements AI (Current version makes the pieces go up and to the left).
 * </p>
 *
 * @author Kacper
 * @version 1.0
 */
package be.kdg.integration2.mvpglobal.model;

import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules.RulesHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AI extends Player {
    private RulesHandler rulesHandler;

    public void setRulesHandler(RulesHandler rulesHandler) {
        this.rulesHandler = rulesHandler;
    }
    public AI(String name, PieceColor color) {
        super(name, color);
    }

    /**
     * Chooses a simple step move that goes upward or leftward.
     * <p>
     * Scans each of this AI’s pieces in row-major order, and for the first
     * one that has a valid single-step move to a lower row or column,
     * returns a {@code Move} from its current position to that destination.
     * </p>
     *
     * @param board the current game board to scan for legal moves
     * @return a Move that goes up or left, or {@code null} if no such move exists
     */
    @Override
    public Move getMove(Board board) {
        List<Move> possibleMoves = generateAllPossibleMoves(board);
        for (Move move : possibleMoves) {
            FactsHandler facts = new FactsHandler(board, move, this.color);

            // Trying rules in order until one is satisfied
            for (int i = 0; i < rulesHandler.numberOfRules(); i++) {
                if (rulesHandler.checkConditionRule(i, facts)) {
                    boolean result = rulesHandler.fireActionRule(i, facts, board, move);
                    if (result) {
                        System.out.println("AI chose move via rule " + i + ": " + move);
                        return move;
                    }
                }
            }
        }

        // Fallback to random move
        if (!possibleMoves.isEmpty()) {

            Random random = new Random();
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        }

        return null; // No moves available
    }
    private List<Move> generateAllPossibleMoves(Board board) {
        List<Move> moves = new ArrayList<>();

        for (int row = 0; row < Board.SIZE; row++) {
            for (int col = 0; col < Board.SIZE; col++) {
                Position from = new Position(row, col);
                Piece piece = board.getPieceAt(from);
                if (piece != null && piece.getColor() == color) {
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            if (dr == 0 && dc == 0) continue;
                            Position to = new Position(row + dr, col + dc);
                            if (board.isValidPosition(to) && board.isEmpty(to)) {
                                moves.add(new Move(from, to));
                            }

                            // Also consider jump moves
                            Position jump = new Position(row + 2 * dr, col + 2 * dc);
                            Position mid = new Position(row + dr, col + dc);
                            if (board.isValidPosition(jump) && board.isEmpty(jump)
                                    && board.getPieceAt(mid) != null) {
                                moves.add(new Move(from, jump));
                            }
                        }
                    }
                }
            }
        }

        return moves;
    }
}