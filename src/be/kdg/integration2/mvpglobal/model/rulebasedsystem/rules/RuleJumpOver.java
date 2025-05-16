package be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactValues;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;

/**
 * This rule handles jump-over moves. It checks whether a move is legal and adds the
 * corresponding fact to the facts handler when the move is legal.
 */
public class RuleJumpOver extends Rule {

    /**
     * Checks if the rule condition is met, which is the availability of the JUMP_OVER fact.
     *
     * @param facts The facts handler containing current facts.
     * @return true if the JUMP_OVER fact is available, false otherwise.
     */
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return facts.factAvailable(FactValues.JUMP_OVER);
    }

    /**
     * Executes the action of the rule, which checks if the move is legal and, if so, adds the
     * JUMP_OVER fact to the facts handler.
     *
     * @param facts  The facts handler containing current facts.
     * @param board  The current game board.
     * @param move   The move being made.
     * @return true if the move is legal and the fact is added, false otherwise.
     */
    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        Position start = move.getStart();
        Position end = move.getEnd();

        // Check if the move is legal
        if (board.isLegalMove(start, end)) {
            // Add the JUMP_OVER fact if the move is legal
            facts.addFact(FactValues.JUMP_OVER);
            return true;
        }

        return false;
    }
}
