package be.kdg.integration2.mvpglobal.view.usernamescreen;

import be.kdg.integration2.mvpglobal.view.gamescreen.GamePresenter;
import be.kdg.integration2.mvpglobal.view.gamescreen.GameView;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class UsernameScreenPresenter {
    private final UsernameScreenView view;
    private final Stage stage;

    public UsernameScreenPresenter(UsernameScreenView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        attachEventHandlers();
    }

    private void attachEventHandlers() {
        view.vsAIButton.setOnAction(e -> {
            String username = view.usernameField.getText().trim();
            if (!username.isEmpty()) {
                startGameAgainstAI(username);
            } else {
                view.promptLabel.setText("Username cannot be empty!");
            }
        });

        view.vsHumanButton.setOnAction(e -> {
            String username = view.usernameField.getText().trim();
            if (!username.isEmpty()) {
                startGameAgainstHuman(username);
            } else {
                view.promptLabel.setText("Username cannot be empty!");
            }
        });

        view.getBackButton().setOnAction(e -> {
            StartScreenView startScreenView = new StartScreenView();
            StartScreenPresenter presenter = new StartScreenPresenter(startScreenView, stage);

            Scene startScreenScene = new Scene(startScreenView, 800, 640);
            stage.setScene(startScreenScene);
            stage.show();
        });
    }

    private void startGameAgainstHuman(String username) {
        view.getVsHumanButton().setOnAction(e -> {
            GameView gameView = new GameView();
            GamePresenter presenter = new GamePresenter(gameView, stage, view.usernameField.getText().trim(), false);
            Scene gameScene = new Scene(gameView, 800, 640);
            stage.setScene(gameScene);
            stage.show();
        });
    }

    private void startGameAgainstAI(String username) {
        view.getVsAIButton().setOnAction(e -> {
            GameView gameView = new GameView();
            GamePresenter presenter = new GamePresenter(gameView, stage, view.usernameField.getText().trim(), true);
            Scene gameScene = new Scene(gameView, 800, 640);
            stage.setScene(gameScene);
            stage.show();
        });
    }

    public UsernameScreenView getView() {
        return view;
    }
}
