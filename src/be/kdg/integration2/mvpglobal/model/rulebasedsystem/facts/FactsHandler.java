package be.kdg.integration2.mvpglobal.model.rulebasedsystem.facts;

import java.util.ArrayList;
import java.util.List;
import be.kdg.integration2.mvpglobal.model.*;


public class FactsHandler {

    private List<FactValues> facts = new ArrayList<>();
    private boolean factsEvolved = false;
    private Board board;
    private Move move;
    private PieceColor color;

    public FactsHandler(Board board, Move move, PieceColor color) {
        this.board = board;
        this.move = move;
        this.color = color;
    }

    public Board getBoard() {
        return board;
    }

    public Move getMove() {
        return move;
    }

    public PieceColor getColor() {
        return color;
    }
    public FactsHandler() {
    }

    public void addFact(FactValues fact) {
        facts.add(fact);
        factsEvolved = true;
    }

    public void removeFact(FactValues fact) {
        facts.remove(fact);
        factsEvolved = true;
    }

    public boolean factsObserved() {
        return !facts.isEmpty();
    }

    public boolean factAvailable(FactValues fact) {
        return facts.contains(fact);
    }

    public void resetFacts() {
        facts.clear();
        factsEvolved = true;
    }

    public boolean factsChanged() {
        return factsEvolved;
    }

    public void setFactsEvolved(Boolean newValue) {
        factsEvolved = newValue;
    }
}

