package be.kdg.integration2.mvpglobal.model.rulebasedsystem;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts.FactsHandler;
import be.kdg.integration2.mvpglobal.model.rulebasedsystem.rules.*;
import java.util.ArrayList;
import java.util.List;

public class InferenceEngine {
    private final List<Rule> rules;

    public InferenceEngine() {
        this.rules = new ArrayList<>();

        rules.add(new RuleGoodMove());
        rules.add(new RuleJumpOver());
        rules.add(new RuleEndMoveAI());
        rules.add(new RuleProgressToGoalAI());

    }

    public boolean isMoveAccepted(Board board, Move move, PieceColor color) {
        FactsHandler facts = new FactsHandler();

        for (Rule rule : rules) {
            if (!rule.actionRule(facts, board, move)) {
                return false; // one rule failed
            }
        }

        return true; // all rules passed
    }

    public Rule getFailedRule(Board board, Move move, PieceColor color) {
        FactsHandler facts = new FactsHandler();

        for (Rule rule : rules) {
            if (!rule.actionRule(facts, board, move)) {
                return rule; // return the first rule that fails
            }
        }

        return null;
    }
}

