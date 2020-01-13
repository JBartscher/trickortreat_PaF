package main.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.Menu.GameOver;
import main.java.Menu.MainMenu;
import main.java.Network.Network;

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
            game.ticks++;
            game.update();

            game.getMapRenderer().drawMap();
            calculateGameTime(startTime);
        }

        public void calculateGameTime(long startTime) {

            if(!game.paused) {
                long endTime = System.currentTimeMillis();

                try {
                    int sleepTime = (int) (1000 / FRAMES - (endTime - startTime));
                    if (sleepTime < 0) sleepTime = 0;
                    Thread.sleep(sleepTime);

                    int gameTime = (int) (game.getGameTime() - (System.currentTimeMillis() - startTime));
                    if (gameTime > 0)
                        game.setGameTime((int) (game.getGameTime() - (System.currentTimeMillis() - startTime)));
                    else {

                        this.stop();
                        new GameOver().showGameOver(game, GameLauncher.this, stage, mainMenu);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public Game getGame() {
        return game;
    }

    public static void main(String[] args){
        launch();
    }
}
