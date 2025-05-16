package be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactValues;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Rule that ensures a good move is made, considering jump-over moves and recently used positions.
 * It gives priority to jump-over moves by checking the corresponding fact.
 */
public class RuleGoodMove extends Rule {

    private static final int BLOCKED_LIMIT = 5;
    private static final Queue<Position> recentlyUsedPositions = new LinkedList<>();

    /**
     * Checks if the rule condition is met. It returns true if the JUMP_OVER fact is available,
     * or if the normal conditions for a good move are met.
     *
     * @param facts The facts handler containing current facts.
     * @return true if the condition is met, false otherwise.
     */
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.JUMP_OVER);
    }

    /**
     * Executes the action of the rule, either prioritizing jump-over moves or checking the legality
     * of the move and blocking previously used positions.
     *
     * @param facts  The facts handler containing current facts.
     * @param board  The current game board.
     * @param move   The move being made.
     * @return true if the action is valid, false otherwise.
     */
    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        if (facts.factAvailable(FactValues.JUMP_OVER)) {
            Position end = move.getEnd();
            boolean legal = board.isLegalMove(move.getStart(), end);
            return legal;
        }

        Position end = move.getEnd();
        boolean legal = board.isLegalMove(move.getStart(), end);
        if (!legal) return false;
//        made it conditional so that below if block can be reached

        if (recentlyUsedPositions.contains(end)) {
            facts.addFact(FactValues.MOVE_BLOCKED);
            return false;
        }

        recentlyUsedPositions.add(end);
        if (recentlyUsedPositions.size() > BLOCKED_LIMIT) {
            recentlyUsedPositions.poll();
        }

        return true;
    }
}
