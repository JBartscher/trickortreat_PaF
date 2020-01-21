package main.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;
import main.java.Menu.GameOver;
import main.java.Menu.MainMenu;
import main.java.Network.Network;
import main.java.Network.NetworkController;
import main.java.map.Tile;

public class GameLauncher extends Application {

    private final static Configuration<Object> config = new Configuration<Object>();
    private Game game;
    private Stage stage;
    private GameLoop gameLoop;
    private MainMenu mainMenu;
    private MovementManager.MovementType movementTypePlayer1;
    private MovementManager.MovementType movementTypePlayer2;
    public final static int FRAMES = ((Number) config.getParam("frames")).intValue();


    public int renderTime = 0;
    public int updateTime = 0;
    public int gesamtTime = 0;

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        mainMenu = new MainMenu(stage, this);
        mainMenu.showMainMenu();
    }


    /**
     * used in locale mode or when server is launching a new game
     * @param gameMode
     * @param networkEngine
     * @param movementTypePlayer1
     * @param movementTypePlayer2
     */
    public void startGame(Game.GameMode gameMode, Network networkEngine, MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {
        this.movementTypePlayer1 = movementTypePlayer1;
        this.movementTypePlayer2 = movementTypePlayer2;
        this.game = new Game(this, stage, gameMode, networkEngine, movementTypePlayer1, movementTypePlayer2);
        gameLoop = new GameLoop();
        game.getWindow().showGameGUI();
        gameLoop.start();
    }

    /** only used by client -> it is not necessary to create a new game object - server sends game object to client who uses this object
     *
      */

    public void startGame(Game game) {
        this.game = game;
        gameLoop = new GameLoop();
        game.getWindow().showGameGUI();
        gameLoop.start();
    }

    /**
     * Super Controller -> renders and updates the game
     */
    private class GameLoop extends AnimationTimer{
        @Override
        public void handle(long now) {
            long startTime = System.currentTimeMillis();
            game.ticks++;
            game.update();

            updateTime += (System.currentTimeMillis() - startTime);
            long middleTime = System.currentTimeMillis();
            game.getMapRenderer().drawMap();
            renderTime += (System.currentTimeMillis() - middleTime);
            calculateGameTime(startTime);

            gesamtTime += (System.currentTimeMillis() - startTime);

            //System.out.println("Gesamtzeit: " + gesamtTime + " ms - UpdateZeit: " + updateTime + " ms - Renderzeit: " + renderTime + " ms");
        }

        /**
         * calculate and update current game time
         * @param startTime
         */
        public void calculateGameTime(long startTime) {

            if(!game.paused) {
                long endTime = System.currentTimeMillis();

                try {
                    int sleepTime = (int) (1000 / FRAMES - (endTime - startTime));

                    if (sleepTime < 0) sleepTime = 0;
                    Thread.sleep(sleepTime);

                    int gameTime = (int) (game.getGameTime() - (System.currentTimeMillis() - startTime));
                    if (gameTime > 0) {
                        game.setGameTime((int) (game.getGameTime() - (System.currentTimeMillis() - startTime)));
                        if (game.getGameTime() < 30000 && !game.DRAMATIC) {
                            game.DRAMATIC = true;
                            Sound.playCountdown();

                            /**
                             * spawn the witch at her home coordinates (door of gingerbread house)
                             */
                            if(game.gameMode == Game.GameMode.LOCAL) {
                                game.getWitch().setyPos(game.getWitch().getyPos() + 2 * Tile.TILE_SIZE);
                            } else if(game.getGameController().getNetworkRole() == NetworkController.NetworkRole.SERVER) {
                                game.getWitch().setyPos(game.getWitch().getyPos() + 2 * Tile.TILE_SIZE);
                            }
                        }
                    }

                    /** set game as DRAMATIC (enables dramatic music and witch movement)
                     *
                     */

                    else {

                        this.stop();
                        new GameOver(game, GameLauncher.this, stage, mainMenu).showGameOver();
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

    /**
     * start the JavaFX application
     * @param args
     */
    public static void main(String[] args){
        launch();
    }
}
