package be.kdg.integration2.mvpglobal.view.statsscreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.List;

public class StatsView extends BorderPane {
    private final Label winnerLabel = new Label();
    private final Label totalTimeLabel = new Label();
    private final Label moveCountLabel = new Label();
    private final Label avgDurationLabel = new Label();

    private final LineChart<Number, Number> durationChart;
    private final Button backButton = new Button("Back");

    public StatsView() {
        setupBackground();
        VBox infoBox = new VBox(10, winnerLabel, totalTimeLabel, moveCountLabel, avgDurationLabel);
        infoBox.setAlignment(Pos.TOP_LEFT);
        infoBox.setPadding(new Insets(20));

        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Move Number");
        yAxis.setLabel("Duration (sec)");
        durationChart = new LineChart<>(xAxis, yAxis);
        durationChart.setTitle("Move Durations");

        VBox centerBox = new VBox(20, infoBox, durationChart, backButton);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(20));

        setCenter(centerBox);
    }

    public void setStats(String winnerName, double totalTime, int moveCount, double avgDuration,
                         List<Double> durations, List<Integer> outlierIndices) {
        winnerLabel.setText("Winner: " + winnerName);
        totalTimeLabel.setText("Total Play Time: " + Math.round(totalTime * 100.0) / 100.0 + " sec");
        moveCountLabel.setText("Total Moves: " + moveCount);
        avgDurationLabel.setText("Average Move Duration: " + Math.round(avgDuration * 100.0) / 100.0 + " sec");

        durationChart.getData().clear();
        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        XYChart.Series<Number, Number> outlierSeries = new XYChart.Series<>();
        series.setName("Durations");
        outlierSeries.setName("Outliers");

        for (int i = 0; i < durations.size(); i++) {
            double val = durations.get(i);
            XYChart.Data<Number, Number> data = new XYChart.Data<>(i + 1, val);
            if (outlierIndices.contains(i)) {
                outlierSeries.getData().add(data);
            } else {
                series.getData().add(data);
            }
        }

        durationChart.getData().addAll(series, outlierSeries);
    }

    public Button getBackButton() {
        return backButton;
    }

    public Parent asParent() {
        return this;
    }

    private void setupBackground() {
        Image backgroundImage = new Image("file:resources/images/backgroundimage.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)
        );
        setBackground(new Background(background));
    }
}
