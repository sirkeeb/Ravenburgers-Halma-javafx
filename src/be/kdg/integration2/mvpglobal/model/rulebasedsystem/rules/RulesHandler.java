package be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules;

import be.kdg.integration2.mvpglobal.model.Board;
import be.kdg.integration2.mvpglobal.model.Move;
import be.kdg.integration2.mvpglobal.model.PieceColor;
import be.kdg.integration2.mvpglobal.model.Position;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;

import java.util.ArrayList;
import java.util.List;

public class RulesHandler {

    private List<Rule> rules = new ArrayList<>();

    /**
     * Order of the rules:
     * 1. if the AI can end the game
     * -> the AI makes this move
     * 2. AI should always move
     * -> Forces aggression
     * 3.the AI can jump over a piece
     * -> the AI should jump over if it can
     * 4. if the AI can make a move to obtain a winning position
     * -> the AI makes this move
     * 5. the AI makes a good move -eg tries to create potential winning position(s)-
     */

    public RulesHandler() {
        rules.add(0, new RuleEndMoveAI());
        rules.add(1, new RuleGoodMove());
        rules.add(2,new RuleJumpOver());
        rules.add(3, new RuleProgressToGoalAI());
    }

    public boolean checkConditionRule(int index, FactsHandler facts) {
        return rules.get(index).conditionRule(facts);
    }

    public boolean fireActionRule(int index, FactsHandler facts, Board board, Move move) {
        return rules.get(index).actionRule(facts, board, move);
    }

    public int numberOfRules() {
        return rules.size();
    }
}


