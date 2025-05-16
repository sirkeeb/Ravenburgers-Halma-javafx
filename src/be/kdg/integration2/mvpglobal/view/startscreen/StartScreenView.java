package be.kdg.integration2.mvpglobal.view.startscreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

public class StartScreenView extends VBox {
    public Button playButton;
    public Button rulesButton;
    public Button leaderboardButton;
    public Button accountCreationButton;
    public Button exitButton;

    public StartScreenView() {
        setWidth(640);
        setHeight(800);
        setAlignment(Pos.CENTER);
        setSpacing(15);
        initialiseNodes();
        layoutNodes();
        setupBackground();
    }

    private void initialiseNodes() {
        playButton = new Button("Play");
        rulesButton = new Button("Rules");
        leaderboardButton = new Button("Leaderboard");
        accountCreationButton = new Button("Create Account");
        exitButton = new Button("Exit");

        playButton.setPrefWidth(200);
        rulesButton.setPrefWidth(200);
        leaderboardButton.setPrefWidth(200);
        accountCreationButton.setPrefWidth(200);
        exitButton.setPrefWidth(200);

        String buttonStyle = "-fx-font-size: 14px;";
        playButton.setStyle(buttonStyle);
        leaderboardButton.setStyle(buttonStyle);
        rulesButton.setStyle(buttonStyle);
        accountCreationButton.setStyle(buttonStyle);
        exitButton.setStyle(buttonStyle);
    }

    private void layoutNodes() {
        Text title = new Text("Halma");
        title.setStyle("""
                -fx-font-size: 48px;
                -fx-font-weight: bold;
                -fx-fill: white;
                """);

        VBox menuBox = new VBox(30);
        menuBox.setAlignment(Pos.CENTER);
        menuBox.getChildren().addAll(
                title,
                playButton,
                rulesButton,
                leaderboardButton,
                accountCreationButton,
                exitButton
        );

        setPadding(new Insets(20));
        getChildren().add(menuBox);
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

    public Button getPlayButton() {
        return playButton;
    }

    public Button getRulesButton() {
        return rulesButton;
    }

    public Button getLeaderboardButton() {
        return leaderboardButton;
    }

    public Button getAccountCreationButton() {
        return accountCreationButton;
    }

    public Button getExitButton() {
        return exitButton;
    }
}
