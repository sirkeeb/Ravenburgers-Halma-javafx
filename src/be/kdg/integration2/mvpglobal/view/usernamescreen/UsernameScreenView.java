package be.kdg.integration2.mvpglobal.view.usernamescreen;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class UsernameScreenView extends VBox {
    public final Label promptLabel     = new Label("Enter your username:");
    public final TextField usernameField = new TextField();
    public final Button vsAIButton     = new Button("Play vs AI");
    public final Button vsHumanButton  = new Button("Play vs Human");
    public final Button backButton     = new Button("Back");

    public UsernameScreenView() {
        setWidth(640);
        setHeight(800);
        setAlignment(Pos.CENTER);
        setSpacing(10);
        usernameField.setMaxWidth(200);
        getChildren().addAll(promptLabel, usernameField, vsAIButton, vsHumanButton, backButton);
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

    public Button getVsAIButton() {
        return vsAIButton;
    }

    public Button getVsHumanButton() {
        return vsHumanButton;
    }

    public Button getBackButton() {
        return backButton;
    }
}
