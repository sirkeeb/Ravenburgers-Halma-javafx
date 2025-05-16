package be.kdg.integration2.mvpglobal.view.startscreen;

import be.kdg.integration2.mvpglobal.view.accountcreationscreen.AccountCreationPresenter;
import be.kdg.integration2.mvpglobal.view.accountcreationscreen.AccountCreationView;
import be.kdg.integration2.mvpglobal.view.leaderboardscreen.LeaderboardPresenter;
import be.kdg.integration2.mvpglobal.view.leaderboardscreen.LeaderboardView;
import be.kdg.integration2.mvpglobal.view.rulesscreen.RulesPresenter;
import be.kdg.integration2.mvpglobal.view.rulesscreen.RulesView;
import be.kdg.integration2.mvpglobal.view.usernamescreen.UsernameScreenPresenter;
import be.kdg.integration2.mvpglobal.view.usernamescreen.UsernameScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StartScreenPresenter {
    private final StartScreenView view;
    private final Stage stage;

    public StartScreenPresenter(StartScreenView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        addEventHandlers();
    }

    private void addEventHandlers() {
        // Play button
        view.getPlayButton().setOnAction(event -> {
            UsernameScreenView usernameScreenView = new UsernameScreenView();
            UsernameScreenPresenter usernameScreenPresenter = new UsernameScreenPresenter(usernameScreenView, stage);
            Scene scene = new Scene(usernameScreenView, 800, 640);
            stage.setScene(scene);
            stage.show();
        });

        // Leaderboard button
        view.getLeaderboardButton().setOnAction(e -> {
            LeaderboardView leaderboardView = new LeaderboardView();
            LeaderboardPresenter leaderboardPresenter = new LeaderboardPresenter(leaderboardView, stage);

            Scene leaderboardScene = new Scene(leaderboardView, 800, 640);
            stage.setScene(leaderboardScene);
            stage.show();
        });

        // Rules Button
        view.getRulesButton().setOnAction(e -> {
            RulesView rulesView = new RulesView();
            RulesPresenter rulesPresenter = new RulesPresenter(rulesView, stage);
            Scene rulesScene = new Scene(rulesView, 800, 640);
            stage.setScene(rulesScene);
            stage.show();
        });

        // Create account button
        view.getAccountCreationButton().setOnAction(e -> {
            AccountCreationView accountCreationView = new AccountCreationView();
            AccountCreationPresenter accountCreationPresenter = new AccountCreationPresenter(accountCreationView, stage);
            Scene accountCreationScene = new Scene(accountCreationView, 800, 640);
            stage.setScene(accountCreationScene);
            stage.show();
        });

        // Exit button
        view.exitButton.setOnAction(e -> System.exit(0));
    }

    public StartScreenView getView() {
        return view;
    }
}
