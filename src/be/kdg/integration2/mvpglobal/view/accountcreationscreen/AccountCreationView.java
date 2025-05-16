package be.kdg.integration2.mvpglobal.view.accountcreationscreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

/**
 * View class responsible for rendering the account creation screen UI.
 * This includes form fields for entering a username and email, and buttons to submit or go back.
 */
public class AccountCreationView extends BorderPane {
    public TextField usernameField;
    public TextField emailField;
    public Button createButton;
    public Button backButton;
    public Label messageLabel;

    /**
     * Constructs the account creation view.
     * Initializes all UI components, sets up layout, and applies background image styling.
     */
    public AccountCreationView() {
        setWidth(640);
        setHeight(800);
        Label title = new Label("Create Account");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        emailField = new TextField();
        emailField.setPromptText("Email");

        createButton = new Button("Create Account");
        backButton = new Button("Back");

        messageLabel = new Label();

        VBox form = new VBox(10, title, usernameField, emailField, createButton, messageLabel, backButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));

        setCenter(form);
        setupBackground();
    }

    /**
     * Sets a background image for the account creation screen.
     * The image is scaled to cover the entire window area.
     */
    private void setupBackground() {
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

    /**
     * Returns the "Back" button component so that it can be wired with event handlers.
     *
     * @return The back navigation button.
     */
    public Button getBackButton() {
        return backButton;
    }
}
