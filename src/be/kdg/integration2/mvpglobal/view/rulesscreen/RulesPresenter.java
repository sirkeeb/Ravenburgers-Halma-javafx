package be.kdg.integration2.mvpglobal.view.rulesscreen;

import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenView;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class RulesPresenter {
    private final RulesView view;
    private final Stage stage;

    public RulesPresenter(RulesView view, Stage stage) {
        this.view = view;
        this.stage = stage;
        addEventHandlers();
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

    public RulesView getView() {
        return view;
    }
}
