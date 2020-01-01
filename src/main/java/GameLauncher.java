package main.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.java.Menu.MainMenu;
import main.java.Menu.MenuTitle;
import main.java.Network.Network;
import main.java.Network.NetworkController;
import main.java.gameobjects.Player;

public class GameLauncher extends Application {

    private Game game;
    public final static int FRAMES = 50;
    private Stage stage;
    private GameLoop gameLoop;
    private MainMenu mainMenu;
    private MovementManager.MovementType movementTypePlayer1;
    private MovementManager.MovementType movementTypePlayer2;

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        mainMenu = new MainMenu(stage, this);
        mainMenu.showMainMenu();
    }


    public void startGame(Game.GameMode gameMode, Network networkEngine, MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {
        this.movementTypePlayer1 = movementTypePlayer1;
        this.movementTypePlayer2 = movementTypePlayer2;
        this.game = new Game(this, stage, gameMode, networkEngine, movementTypePlayer1, movementTypePlayer2);
        gameLoop = new GameLoop();
        game.getWindow().showGameGUI();
        gameLoop.start();
    }

    // Remote - it is not necessary to create a new game object - server sends game object to client who uses this object
    public void startGame(Game game) {
        this.game = game;
        gameLoop = new GameLoop();
        game.getWindow().showGameGUI();
        gameLoop.start();
    }

    private class GameLoop extends AnimationTimer{
        @Override
        public void handle(long now) {
            long startTime = System.currentTimeMillis();
            game.update();
            game.getMapRenderer().drawMap();
            long endTime = System.currentTimeMillis();
            //System.out.println("Benoetigte Zeit: " + (endTime - startTime));

            try {
                int sleepTime = (int)(1000 / FRAMES - (endTime - startTime));
                if(sleepTime < 0) sleepTime = 0;
                Thread.sleep(sleepTime);

                int gameTime = (int) (game.getGameTime() - (System.currentTimeMillis() - startTime));
                if(gameTime > 0)
                    game.setGameTime((int)(game.getGameTime() - (System.currentTimeMillis() - startTime)));
                else {
                    showGameOver();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // TODO : SEHR HÄSSLICH GECODET - aber erstmal funktional - auslagern wäre sinnvoll
    public void showGameOver() {

        this.gameLoop.stop();
        this.gameLoop = null;

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
        } else {
            if(game.getNetworkController().getNetworkRole() == NetworkController.NetworkRole.SERVER ) {
                player1 = game.getPlayer();
                player2 = game.getOtherPlayer();

            } else {
                player1 = game.getOtherPlayer();
                player2 = game.getPlayer();
            }
        }




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

        Text textCandyPlayer1 = new Text(String.valueOf(player1.getCandy()) + "x");
        GraphicsUtility.setTextProperties(textCandyPlayer1, "-fx-font: 40 arial;", Color.WHITE, Window.WIDTH / 4 - 50, Window.HEIGHT - 100);


        Text textCandyPlayer2 = new Text(String.valueOf(player2.getCandy())+ "x");
        GraphicsUtility.setTextProperties(textCandyPlayer2, "-fx-font: 40 arial;", Color.WHITE, Window.WIDTH / 4 * 3 + 10, Window.HEIGHT - 100);


        Button buttonRestart = new Button("Restart the game");
        Button buttonMainMenu = new Button("Back to main menu");

        buttonRestart.setOnAction( (e) -> {
            if(game.gameMode == Game.GameMode.LOCAL) {
                startGame(game.gameMode, null, movementTypePlayer1, movementTypePlayer2);
            }
            // TODO: Replay von Client / Server implementieren
            else {
                //startGame(game.gameMode, game.getNetworkController().getNetworkEngine(), movementTypePlayer1, movementTypePlayer2);
            }

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

    public Game getGame() {
        return game;
    }

    public static void main(String[] args){
        launch();
    }
}
