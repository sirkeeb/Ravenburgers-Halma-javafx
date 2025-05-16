package be.kdg.integration2.mvpglobal.view.statsscreen;

import be.kdg.integration2.mvpglobal.model.DatabaseConnections;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.List;
import java.util.Map;

public class StatsPresenter {
    private final StatsView view;
    private final Stage stage;
    private final DatabaseConnections db;
    private final int userId;
    private final boolean humanWinner;

    public StatsPresenter(StatsView view, Stage stage, int userId, boolean humanWinner) {
        this.view = view;
        this.stage = stage;
        this.db = new DatabaseConnections();
        this.userId = userId;
        this.humanWinner = humanWinner;

        loadStats();
        attachHandlers();
    }

    private void attachHandlers() {
        view.getBackButton().setOnAction(e -> {
            db.closeConnection();
            StartScreenView startScreenView = new StartScreenView();
            StartScreenPresenter presenter = new StartScreenPresenter(startScreenView, stage);

            Scene startScreenScene = new Scene(startScreenView, 800, 640);
            stage.setScene(startScreenScene);
            stage.show();
        });
    }

    private void loadStats() {
        String winner = null;
        Map<String, Object> stats = db.getDetailedGameStats(userId);

        if (humanWinner) {
            boolean isHumanWinner = (boolean) stats.get("winner");
            winner = isHumanWinner ? db.getUsernameById(userId) : "AI";
        } else {
            winner = "AI";
        }

        double totalTime = (double) stats.get("totalTime");
        int moveCount = (int) stats.get("moveCount");
        double avg = (double) stats.get("average");
        List<Double> durations = (List<Double>) stats.get("durations");
        List<Integer> outliers = (List<Integer>) stats.get("outliers");

        view.setStats(winner, totalTime, moveCount, avg, durations, outliers);
    }

    public StatsView getView() {
        return view;
    }
}
