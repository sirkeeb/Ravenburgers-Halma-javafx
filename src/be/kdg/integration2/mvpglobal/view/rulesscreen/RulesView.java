package be.kdg.integration2.mvpglobal.view.rulesscreen;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.*;

public class RulesView extends BorderPane {
    public final TextArea rulesArea = new TextArea();
    public final Button backButton = new Button("Back to Menu");

    public RulesView() {
        setWidth(640);
        setHeight(800);
        rulesArea.setWrapText(true);
        rulesArea.setEditable(false);
        rulesArea.setText(
                "Halma Rules\n" +
                        "1. Objective\n" +
                        "Move all of your pieces from your starting corner to occupy the opposite corner before your opponent does.\n" +
                        "\n" +
                        "2. Setup\n" +
                        "Board: 16×16 grid (or 8×8 for smaller “Chinese Checkers” variants).\n" +
                        "\n" +
                        "Players: 2. Black starts in the top-left 4×4 corner; White in the bottom-right 4×4 corner.\n" +
                        "\n" +
                        "Pieces: Each player has 19 (on a 16×16 board) or 10 (on 8×8).\n" +
                        "\n" +
                        "3. Turn Sequence\n" +
                        "Players alternate turns.\n" +
                        "\n" +
                        "On your turn, move exactly one piece.\n" +
                        "\n" +
                        "4. Legal Moves\n" +
                        "Single-step: Move one square into any of the eight surrounding spaces (orthogonal or diagonal) if it’s empty.\n" +
                        "\n" +
                        "Jump: You may jump over one adjacent occupied square (friend or foe) into the empty square immediately beyond.\n" +
                        "\n" +
                        "You can make multiple consecutive jumps in a single turn, chaining them together (but must land in an empty square each time).\n" +
                        "\n" +
                        "Direction can change between jumps.\n" +
                        "\n" +
                        "5. Restrictions\n" +
                        "You may not combine a single-step and jumps in the same turn.\n" +
                        "\n" +
                        "You may only jump over one adjacent piece at a time.\n" +
                        "\n" +
                        "6. Winning the Game\n" +
                        "The first player to have all their pieces occupy the entire destination camp (your opponent’s starting corner) wins immediately.");
        rulesArea.setStyle("-fx-background-color: #f8eac9; -fx-text-fill: black; -fx-control-inner-background: #f8eac9;");
        rulesArea.setOpacity(0.85);
        setCenter(rulesArea);
        BorderPane.setMargin(rulesArea, new Insets(10));

        HBox bottom = new HBox(backButton);
        bottom.setAlignment(Pos.CENTER);
        bottom.setPadding(new Insets(10));
        setBottom(bottom);
        setupBackground();
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

    public Button getBackButton() {
        return backButton;
    }
}
