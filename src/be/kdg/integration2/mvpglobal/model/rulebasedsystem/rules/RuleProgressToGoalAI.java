package be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactValues;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;

import java.util.Set;

/**
 * Rule that ensures AI progresses towards its goal by only moving pieces closer to its target area
 * and prevents moving away from the goal unless moving deeper into the winning camp.
 */
public class RuleProgressToGoalAI extends Rule {

    /**
     * Evaluates whether the condition for the rule is met. In this case, the condition is always true.
     *
     * @param facts The facts handler containing the current game facts.
     * @return true, as the condition is always met in this rule.
     */
    @Override
    public boolean conditionRule(FactsHandler facts) {
        return true;
    }

    /**
     * Executes the action for the rule by checking if the AI move progresses toward the goal.
     * It prevents the AI piece from moving out of its winning camp unless moving deeper into it,
     * and ensures that the AI only makes moves that bring it closer to its target area.
     *
     * @param facts The facts handler containing the current game facts.
     * @param board The current game board.
     * @param move  The move being made.
     * @return true if the move progresses towards the goal, false otherwise.
     */
    @Override
    public boolean actionRule(FactsHandler facts, Board board, Move move) {
        Position start = move.getStart();
        Position end = move.getEnd();

        boolean startInCamp = isInWinningCamp(start);
        boolean endInCamp = isInWinningCamp(end);

        int startDistance = manhattanDistance(start, new Position(0, 0));
        int endDistance = manhattanDistance(end, new Position(0, 0));

        // Only allow moves out of the camp if it moves deeper into the camp
        if (startInCamp && !endInCamp) {
            return false;
        }

        // Avoid regression — only accept moves that are closer to goal
        if (endDistance < startDistance) {
            facts.addFact(FactValues.PROGRESS_AI);
            return true;
        }

        return false;
    }

    /**
     * Calculates the Manhattan distance between two positions on the board.
     *
     * @param a The starting position.
     * @param b The destination position.
     * @return The Manhattan distance between the two positions.
     */
    private int manhattanDistance(Position a, Position b) {
        return Math.abs(a.getRow() - b.getRow()) + Math.abs(a.getCol() - b.getCol());
    }

    /**
     * Checks if the given position is within the AI's winning camp.
     * For this case, the AI's winning camp is defined by positions where row <= 3 and col <= row.
     *
     * @param pos The position to check.
     * @return true if the position is in the AI's winning camp, false otherwise.
     */
    private boolean isInWinningCamp(Position pos) {
         final Set<Position> blackCamp = Set.of(
            new Position(0,0), new Position(0,1), new Position(0,2), new Position(0,3), new Position(0,4),
            new Position(1,0), new Position(1,1), new Position(1,2), new Position(1,3), new Position(1,4),
            new Position(2,0), new Position(2,1), new Position(2,2), new Position(2,3),
            new Position(3,0), new Position(3,1), new Position(3,2),
            new Position(4,0), new Position(4,1)
    );
        return blackCamp.contains(pos);
    }
}
