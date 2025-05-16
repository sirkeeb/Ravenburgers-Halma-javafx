package be.kdg.integration2.mvpglobal.view.leaderboardscreen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

import java.util.Map;

public class LeaderboardView extends BorderPane {
    public final TableView<Map<String, Object>> table = new TableView<>();
    public final Button backButton = new Button("Back to Menu");

    public LeaderboardView() {
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        setCenter(table);

        HBox bottom = new HBox(backButton);
        bottom.setAlignment(Pos.CENTER);
        setBottom(bottom);
        setupBackground();
    }

    private void setupBackground() {
        // Create background image
        Image backgroundImage = new Image("file:resources/images/backgroundimage.jpg");
        BackgroundImage background = new BackgroundImage(
                backgroundImage,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1.0, 1.0, true, true, false, true)  // Cover the entire window
        );

        setBackground(new Background(background));
    }

    public Button getBackButton() {
        return backButton;
    }
}
