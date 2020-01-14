package main.java.ui;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.*;
import main.java.gameobjects.Player;

public class GameMenu {

    /**
     * Menu components
     */
    private static final Image backgroundImage = new Image(GameMenu.class.getResourceAsStream("headline.png"));
    /**
     * Singleton instance
     */
    private static GameMenu instance;
    private static ImageView backgroundImageView = new ImageView(backgroundImage);
    private static ImageView imageCandyPlayer = new ImageView(GraphicsUtility.getCandyImage());
    private static ImageView imageCandyOtherPlayer = new ImageView(GraphicsUtility.getCandyImage());
    private final String TEXT_STYLE = "-fx-font: 32 arial;";
    private final String TEXT_STYLE_SMALL = "-fx-font: 16 arial;";
    private Text candyTextPlayer = new Text("Spieler 1 - Candy: " + 0);
    private Text candyTextOtherPlayer = new Text("Spieler 2 - Candy: " + 0);
    private Text timerText = new Text("");
    private Text textSound = new Text("MUTE SOUND with KEY M");
    /**
     * Observers
     */
    private PlayerScoreObserver firstPlayerObserver = new PlayerScoreObserver();
    private OtherPlayerScoreObserver secondPlayerObserver = new OtherPlayerScoreObserver();

    /**
     * private GameMenu constructor, can only be called by getInstance().
     * <p>
     * Initializes all menu components (size, color, font, placing, etc.).
     */
    private GameMenu() {
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

    /**
     * return a Singleton Instance of the GameMenu class
     *
     * @return the single GameMenu Instance
     */
    public static GameMenu getInstance() {
        if (GameMenu.instance == null)
            GameMenu.instance = new GameMenu();

        return GameMenu.instance;
    }

    /**
     * adds all components of the menu to its parent scene element
     *
     * @param root the scene which will be drawn
     */
    public void addGameMenuToScene(Group root) {
        root.getChildren().addAll(backgroundImageView, candyTextPlayer, candyTextOtherPlayer, timerText, textSound, imageCandyPlayer, imageCandyOtherPlayer);
    }

    /**
     * updates the candyScore Text Element of the first player
     *
     * @param i Integer candy
     */
    private void updatePlayerCandyScore(int i) {
        candyTextPlayer.setText(String.format("Spieler 1 - Candy: %d", i));
    }

    /**
     * updates the candyScore Text Element of the second player
     *
     * @param i Integer candy
     */
    private void updateOtherPlayerCandyScore(int i) {
        candyTextOtherPlayer.setText(String.format("Spieler 2 - Candy: %d", i));
    }

    /**
     * updates the timer Text Element of this menu
     *
     * @param s String sconds left
     */
    public void updateTimeText(String s) {
        timerText.setText(s);
    }

    /**
     * returns the observer for the candyScore Text of the first Player,
     * the method has to be public to make it possible to be added by the player in
     * GameController.initEntities
     *
     * @return firstPlayerObserver
     * @see GameController#initEntities(MovementManager.MovementType, MovementManager.MovementType)
     */
    public PlayerScoreObserver getFirstPlayerObserver() {
        return firstPlayerObserver;
    }

    /**
     * returns the observer for the candyScore Text of the second Player,
     * the method has to be public to make it possible to be added by the player in
     * GameController.initEntities
     *
     * @return secondPlayerObserver
     * @see GameController#initEntities(MovementManager.MovementType, MovementManager.MovementType)
     */
    public OtherPlayerScoreObserver getSecondPlayerObserver() {
        return secondPlayerObserver;
    }

    /**
     * Observer - candyScore Text of the first Player
     */
    class PlayerScoreObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            Player p = (Player) o;
            GameMenu.getInstance().updatePlayerCandyScore(p.getCandy());
        }
    }

    /**
     * Observer - candyScore Text of the second Player
     */
    class OtherPlayerScoreObserver implements Observer {
        @Override
        public void update(Observable o, Object arg) {
            Player p = (Player) o;
            GameMenu.getInstance().updateOtherPlayerCandyScore(p.getCandy());
        }
    }
}
