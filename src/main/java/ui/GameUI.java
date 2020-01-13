package main.java.ui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.GraphicsUtility;
import main.java.Sound;
import main.java.Window;

import java.util.Observable;
import java.util.Observer;

public class GameUI implements Observer {

    static final Image backgroundImage = new Image(GameUI.class.getResourceAsStream("headline.png"));
    static ImageView backgroundImageView = new ImageView(backgroundImage);
    static ImageView imageCandyPlayer = new ImageView(GraphicsUtility.getCandyImage());
    static ImageView imageCandyOtherPlayer = new ImageView(GraphicsUtility.getCandyImage());
    private final String TEXT_STYLE = "-fx-font: 32 arial;";
    private final String TEXT_STYLE_SMALL = "-fx-font: 16 arial;";
    Text candyTextPlayer = new Text("Spieler 1 - Candy: " + 0);
    Text candyTextOtherPlayer = new Text("Spieler 2 - Candy: " + 0);
    Text timerText = new Text("WHAT THE FUCK IS GOING ON?!!?");
    Text textSound = new Text("MUTE SOUND with KEY M");

    public GameUI() {
        // Timer Text
        timerText.setStyle(TEXT_STYLE_SMALL);
        GraphicsUtility.setTextProperties(timerText, TEXT_STYLE_SMALL, Color.WHITE, Window.WIDTH / 2 - 10, 50.0); //
        //timerText.setStrokeWidth(5.0);
        timerText.setId("timerText");

        // Player CandyScore Text
        candyTextPlayer.setStyle(TEXT_STYLE);
        GraphicsUtility.setTextProperties(candyTextPlayer, TEXT_STYLE, Color.WHITE, 150, 50.0);
        candyTextPlayer.setId("candyTextPlayer");

        // OtherPlayer CandyScore Text
        candyTextOtherPlayer.setStyle(TEXT_STYLE);
        GraphicsUtility.setTextProperties(candyTextOtherPlayer, TEXT_STYLE, Color.WHITE, 800, 50.0);
        candyTextOtherPlayer.setId("candyTextOtherPlayer");

        // Text Sound;
        GraphicsUtility.setTextProperties(textSound, TEXT_STYLE_SMALL, Color.WHITE, Window.WIDTH / 2 - 80, 70);
        textSound.setOnMouseClicked((e) ->
        {
            Sound.muteSound();
        });
        textSound.setId("textSound");

        // Lolipop Icon Player
        GraphicsUtility.setImageProperties(imageCandyPlayer, Window.WIDTH / 3 + 40, 22);
        // Lolipop Icon OtherPlayer
        GraphicsUtility.setImageProperties(imageCandyOtherPlayer, 1120, 22);
    }

    public void addGameMenuToScene(Group root) {
        root.getChildren().addAll(backgroundImageView, candyTextPlayer, candyTextOtherPlayer, timerText, textSound, imageCandyPlayer, imageCandyOtherPlayer);
    }

    public void updateCandyTextPlayer(int i) {
        candyTextPlayer.setText(String.format("Spieler 1 - Candy: ", i));
    }

    public void updateCandyTextOtherPlayer(int i) {
        candyTextOtherPlayer.setText(String.format("Spieler 2 - Candy: ", i));
    }

    public void updateTimeText(String s) {
        timerText.setText(s);
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
