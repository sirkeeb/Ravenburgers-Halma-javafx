package be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactValues;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;

/**
 * Rule that triggers when the AI reaches the goal area (top-left triangle).
 * The AI is always considered white, and the rule checks if the AI's move
 * ends in the top-left triangle, signaling the completion of the goal.
 *
 * The condition for this rule to be applicable is that the move should
 * end in the goal area of the AI (white).
 */
public class RuleEndMoveAI extends Rule {

    /**
     * Checks if the rule condition is met. In this case, it always returns false
     * since the condition is checked in the `actionRule` method.
     *
     * @param facts The facts handler containing the current facts.
     * @return false since the condition is evaluated based on the move.
     */
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return false;
    }

    /**
     * Executes the action of the rule. If the AI's move ends in the goal area (top-left triangle),
     * the fact indicating the AI's end move is added to the facts handler.
     *
     * @param facts  The facts handler containing current facts.
     * @param board  The current game board.
     * @param move   The move being made.
     * @return true if the move ends in the goal area, false otherwise.
     */
    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        Position end = move.getEnd();

        // AI is always WHITE, so check if move ends in the WHITE goal area (top-left triangle)
        if (end.getRow() < 4 && end.getCol() < 4) {
            facts.addFact(FactValues.ENDMOVE_AI); // Add the fact indicating AI's move is completed
            return true;
        }
        return false;
    }
}
