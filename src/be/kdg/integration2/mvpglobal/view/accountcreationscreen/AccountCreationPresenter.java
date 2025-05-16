package be.kdg.integration2.mvpglobal.view.accountcreationscreen;

import be.kdg.integration2.mvpglobal.model.DatabaseConnections;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Presenter class for the Account Creation screen in the MVP architecture.
 * Handles user interaction, input validation, and view navigation.
 */
public class AccountCreationPresenter {
    private final Stage stage;
    private final AccountCreationView view;
    private final DatabaseConnections db;

    /**
     * Constructs an AccountCreationPresenter with the given view and stage.
     *
     * @param view  the view associated with the account creation screen
     * @param stage the primary stage of the JavaFX application
     */
    public AccountCreationPresenter(AccountCreationView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        this.db = new DatabaseConnections();
        attachHandlers();
    }

    /**
     * Attaches event handlers to the view's buttons.
     * Handles user creation and navigation back to the start screen.
     */
    private void attachHandlers() {
        view.createButton.setOnAction(e -> handleCreate());

        view.getBackButton().setOnAction(e -> {
            StartScreenView startScreenView = new StartScreenView();
            StartScreenPresenter presenter = new StartScreenPresenter(startScreenView, stage);

            Scene startScreenScene = new Scene(startScreenView, 800, 640);
            stage.setScene(startScreenScene);
            stage.show();
        });
    }

    /**
     * Handles the account creation logic.
     * Validates input and inserts a new user into the database if valid.
     */
    private void handleCreate() {
        String username = view.usernameField.getText().trim();
        String email = view.emailField.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            view.messageLabel.setText("please enter name and email");
            return;
        }

        db.insertUser(username, email);
        view.messageLabel.setText("account created");
    }
}
