package be.kdg.integration2.mvpglobal.view.leaderboardscreen;

import be.kdg.integration2.mvpglobal.model.DatabaseConnections;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenView;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.stage.Stage;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class LeaderboardPresenter {
    private final LeaderboardView view;
    private final DatabaseConnections db;
    private final Stage stage;

    public LeaderboardPresenter(LeaderboardView view, Stage stage) {
        this.view    = view;
        this.stage = stage;
        this.db      = new DatabaseConnections();

        db.createTables();
        addEventHandlers();
        loadLeaderboard();
    }

    private void addEventHandlers() {
        view.getBackButton().setOnAction(e -> {
            StartScreenView startScreenView = new StartScreenView();
            StartScreenPresenter presenter = new StartScreenPresenter(startScreenView, stage);

            Scene startScreenScene = new Scene(startScreenView, 800, 640);
            stage.setScene(startScreenScene);
            stage.show();
        });
    }

    private void loadLeaderboard() {
        view.table.getColumns().clear();

        String[] keys    = {
                "username", "total_games", "total_wins", "total_losses",
                "win_percent", "avg_turns", "avg_duration"
        };
        String[] headers = {
                "User", "Games", "Wins", "Losses", "% Won", "Avg Moves", "Avg Turn Time (s)"
        };

        for (int i = 0; i < keys.length; i++) {
            final String key = keys[i];
            TableColumn<Map<String,Object>,Object> col = new TableColumn<>(headers[i]);
            col.setCellValueFactory(cellData ->
                    new SimpleObjectProperty<>(cellData.getValue().get(key))
            );
            view.table.getColumns().add(col);
        }

        if (!db.dbAvailable) {
            showDatabaseError();
            view.table.setItems(FXCollections.emptyObservableList());
            return;
        }

        String sql = """
            SELECT
              u.username,
              COUNT(g.game_id) AS total_games,
              SUM(CASE WHEN g.is_win THEN 1 ELSE 0 END) AS total_wins,
              SUM(CASE WHEN g.is_win THEN 0 ELSE 1 END) AS total_losses,
              ROUND((100.0 * SUM(CASE WHEN g.is_win THEN 1 ELSE 0 END) / NULLIF(COUNT(g.game_id),0))::numeric, 1) AS win_percent,
              ROUND((AVG(mv.moves_per_game))::numeric, 1) AS avg_turns,
              ROUND((AVG(mv.duration_per_move))::numeric, 3) AS avg_duration
            FROM users u
            LEFT JOIN games g
              ON u.user_id = g.user_id
            LEFT JOIN (
              SELECT
                game_id,
                COUNT(*)     AS moves_per_game,
                AVG(duration) AS duration_per_move
              FROM moves
              GROUP BY game_id
            ) mv
              ON g.game_id = mv.game_id
            GROUP BY u.username
            ORDER BY total_wins DESC, total_games ASC;
        """;

        ObservableList<Map<String,Object>> data = FXCollections.observableArrayList();
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Map<String,Object> row = new HashMap<>();
                for (String key : keys) {
                    row.put(key, rs.getObject(key));
                }
                data.add(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showDatabaseError();
        }

        view.table.setItems(data);
    }

    private void showDatabaseError() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Leaderboard Unavailable");
        alert.setHeaderText(null);
        alert.setContentText("Cannot reach the database right now. Showing empty leaderboard.");
        alert.showAndWait();
    }

    public LeaderboardView getView() {
        return view;
    }
}
