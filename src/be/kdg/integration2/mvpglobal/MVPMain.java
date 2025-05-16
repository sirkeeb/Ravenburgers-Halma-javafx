package be.kdg.integration2.mvpglobal;

import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenPresenter;
import be.kdg.integration2.mvpglobal.view.startscreen.StartScreenView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MVPMain extends Application {
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StartScreenView startScreenView = new StartScreenView();
        StartScreenPresenter startScreenPresenter = new StartScreenPresenter(startScreenView, primaryStage);

        Scene scene = new Scene(startScreenView, 800, 640);
        primaryStage.setTitle("Halma");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
