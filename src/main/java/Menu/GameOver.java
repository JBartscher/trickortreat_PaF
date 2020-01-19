package main.java.Menu;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.Game;
import main.java.GameLauncher;
import main.java.Network.ClientEngine;
import main.java.Network.Event;
import main.java.Network.NetworkController;
import main.java.Network.ServerEngine;
import main.java.Sound;
import main.java.Window;
import main.java.gameobjects.Player;
import main.java.sprites.GraphicsUtility;

public class GameOver {


    private Game game;
    private GameLauncher gameLauncher;
    private Stage stage;
    private MainMenu mainMenu;

    public static boolean restart;
    public static String text ="Send Replay-Request";

    public static Button buttonRestart;

    public GameOver(Game game, GameLauncher gameLauncher, Stage stage, MainMenu mainMenu) {

        this.game = game;
        this.gameLauncher = gameLauncher;
        this.stage = stage;
        this.mainMenu = mainMenu;
    }

    public void showGameOver() {

        Pane root = new Pane();
        //root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, Window.WIDTH, Window.HEIGHT);


        Player player1;
        Player player2;

        // Obwohl der Client in Wirklichkeit Spieler 2 ist, arbeitet die GameLogik so, dass der eigene Spieler immer Spieler 1 ist
        // Daher werden, sofern GameOVER als Client aufgerufen wird, Player 1 und Player 2 vertauscht
        // Dies dient dazu, dass die Anzeige der gesammelten Süßigkeiten den korrekten Spielern zugeordnet wird

        if(game.getGameMode() == Game.GameMode.LOCAL) {
            player1 = game.getPlayer();
            player2 = game.getOtherPlayer();
            new HighScoreGUI().checkScore(new int[]{player1.getCandy(), player2.getCandy()}, 0, false);
        } else {
            if(game.getGameController().getNetworkRole() == NetworkController.NetworkRole.SERVER ) {
                player1 = game.getPlayer();
                player2 = game.getOtherPlayer();
                new HighScoreGUI().checkScore(new int[]{player1.getCandy()}, 0, false);

            } else {
                player1 = game.getOtherPlayer();
                player2 = game.getPlayer();
                new HighScoreGUI().checkScore(new int[]{player2.getCandy()}, 0, false);
            }
        }

        Sound.playGameover();

        ImageView imageView = new ImageView(new Image(MainMenu.class.getResource("main_menu_converted.png").toExternalForm()));
        imageView.setOpacity(0.7);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.8);
        imageView.setEffect(colorAdjust);

        imageView.setFitWidth(Window.WIDTH);
        imageView.setFitHeight(Window.HEIGHT);

        MenuTitle gameOverTitle = new MenuTitle("GAME OVER", 48);
        gameOverTitle.setTranslateX(Window.WIDTH / 2 - 200);
        gameOverTitle.setTranslateY(Window.HEIGHT / 4);


        ImageView imageViewPlayer1 = new ImageView(player1.getGameOverSpriteImage());
        ImageView imageViewPlayer2 = new ImageView(player2.getGameOverSpriteImage());

        imageViewPlayer1.setScaleX(2);
        imageViewPlayer1.setScaleY(2);
        GraphicsUtility.setImageProperties(imageViewPlayer1, Window.WIDTH / 4 - 50, Window.HEIGHT - 350);


        imageViewPlayer2.setScaleX(2);
        imageViewPlayer2.setScaleY(2);
        GraphicsUtility.setImageProperties(imageViewPlayer2, Window.WIDTH / 4 * 3, Window.HEIGHT - 350);

        Text textPlayer1 = new Text("SPIELER 1");
        GraphicsUtility.setTextProperties(textPlayer1, "-fx-font: 40 arial;", Color.WHITE, Window.WIDTH / 4 - 110, Window.HEIGHT - 430);

        Text textPlayer2 = new Text("SPIELER 2");
        GraphicsUtility.setTextProperties(textPlayer2, "-fx-font: 40 arial;", Color.WHITE, Window.WIDTH / 4 * 3 - 70, Window.HEIGHT - 430);

        ImageView imageViewCandy1 = new ImageView(GraphicsUtility.getCandyImage());
        GraphicsUtility.setImageProperties(imageViewCandy1, Window.WIDTH / 4 - 50, Window.HEIGHT - 200);
        imageViewCandy1.setScaleX(3);
        imageViewCandy1.setScaleY(3);


        ImageView imageViewCandy2 = new ImageView(GraphicsUtility.getCandyImage());
        GraphicsUtility.setImageProperties(imageViewCandy2, Window.WIDTH / 4 * 3 + 10, Window.HEIGHT - 200);
        imageViewCandy2.setScaleX(3);
        imageViewCandy2.setScaleY(3);

        Text textCandyPlayer1 = new Text(player1.getCandy() + "x");
        GraphicsUtility.setTextProperties(textCandyPlayer1, "-fx-font: 40 arial;", Color.WHITE, Window.WIDTH / 4 - 50, Window.HEIGHT - 100);


        Text textCandyPlayer2 = new Text(player2.getCandy() + "x");
        GraphicsUtility.setTextProperties(textCandyPlayer2, "-fx-font: 40 arial;", Color.WHITE, Window.WIDTH / 4 * 3 + 10, Window.HEIGHT - 100);


        buttonRestart = new Button(text);
        Button buttonMainMenu = new Button("Back to main menu");

        buttonRestart.setOnAction( (e) -> {

            if(game.gameMode == Game.GameMode.LOCAL) {
                gameLauncher.startGame(game.gameMode, null, game.getPlayer().getMovementType(), game.getOtherPlayer().getMovementType());
            }
            // TODO: Replay von Client / Server implementieren
            else {


                NetworkController networkController = (NetworkController)game.getGameController();
                NetworkController.NetworkRole networkRole = networkController.getNetworkRole();

                if(networkRole == NetworkController.NetworkRole.SERVER) {
                    if(!ClientEngine.restart) {
                        GameOver.setMessage("Wait for client respond");
                        ServerEngine.restart = true;
                        networkController.changeGameStateObject("", Event.EventType.REPLAY);
                    }

                } else if(networkRole == NetworkController.NetworkRole.CLIENT) {
                    if(!ServerEngine.restart) {
                        GameOver.setMessage("Wait for server respond ");
                        ClientEngine.restart = true;
                        networkController.changeGameStateObject("", Event.EventType.REPLAY);
                    }
                }


                //startGame(game.gameMode, game.getNetworkController().getNetworkEngine(), movementTypePlayer1, movementTypePlayer2);
            }
            Sound.playMusic();
        });

        buttonMainMenu.setOnAction( (e) -> {
            mainMenu.showMainMenu();
        });

        buttonMainMenu.setTranslateX(Window.WIDTH /  2 - 130);
        buttonMainMenu.setStyle("-fx-font: 24 arial;");
        buttonMainMenu.setTranslateY(600);

        buttonRestart.setTranslateX(Window.WIDTH / 2 - 130);
        buttonRestart.setStyle("-fx-font: 24 arial;");
        buttonRestart.setTranslateY(500);

        root.getChildren().addAll(imageView, gameOverTitle, imageViewPlayer1, imageViewPlayer2, textPlayer1, textPlayer2, imageViewCandy1, imageViewCandy2, textCandyPlayer1, textCandyPlayer2, buttonMainMenu, buttonRestart);


        stage.setScene(scene);
    }

    public static void setMessage(String message){
        Platform.runLater( () -> {
            buttonRestart.setText(message);
        });
    }
}
