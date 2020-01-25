package main.java;

import javafx.animation.AnimationTimer;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import main.java.menu.GameMenu;
import main.java.menu.GameOver;
import main.java.network.ClientEngine;
import main.java.network.NetworkController;
import main.java.gameobjects.AliceCooper;
import main.java.gameobjects.Player;
import main.java.gameobjects.Witch;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.TownHall;
import main.java.map.MapObject;
import main.java.map.Tile;
import main.java.network.ServerEngine;

import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * super controller that contains the game loop which updates the game and delegates tasks to the single components
 */
public class GameController implements Observer {

    protected Game game;
    protected GameLauncher gameLauncher;
    protected GameLoop gameLoop = new GameLoop();

    public GameController(Game game, GameLauncher gameLauncher) {
        this.game = game;
        this.gameLauncher = gameLauncher;
    }

    public void initGUIandSound(Stage stage) {
        // GUI-Bereich
        game.setStage(stage);
        game.setWindow(new Window(game, stage));
        game.getWindow().createGUI();
        game.setMapRenderer(new MapRenderer(game.getMap(), game.getWindow(), game));
        game.setGameCamera(new GameCamera(game.getMap().getSize(), game.getMap().getSize(), game.getPlayer()));
        game.setMovementManager(new MovementManager(game, game.getPlayer(), game.getOtherPlayer()));

        game.getPlayer().notifyObservers(game.getPlayer());
        game.getOtherPlayer().notifyObservers(game.getOtherPlayer());

        // Movement - Weiterleiten an Controller-Klasse
        game.getWindow().getScene().addEventHandler(InputEvent.ANY, game.getMovementManager());

    }

    /**
     * the players have now each a Observer to change the Score when visiting a house.
     *
     * @param movementTypePlayer1
     * @param movementTypePlayer2
     */
    public void initEntities(MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {

        game.setPlayer(new Player(movementTypePlayer1));
        game.getPlayer().setxPos(game.getMap().getSize() * 0.5 * Tile.TILE_SIZE);
        game.getPlayer().setyPos((game.getMap().getSize() * 0.5 + 3) * Tile.TILE_SIZE);
        game.getPlayer().setTarget(new Point((int) game.getPlayer().getxPos(), (int) game.getPlayer().getyPos()));

        game.getListOfPlayers().add(game.getPlayer());
        // add Score-Observer which notifyes its Score text when this player visits a house
        game.getPlayer().addObserver(GameMenu.getInstance().getFirstPlayerObserver());

        game.setOtherPlayer(new Player(movementTypePlayer2));
        // add Score-Observer which notifyes its Score text when this otherPlayer visits a house
        game.getOtherPlayer().addObserver(GameMenu.getInstance().getSecondPlayerObserver());
        game.getListOfPlayers().add(game.getOtherPlayer());
        Game.WIDTH = Window.WIDTH / 2 - Tile.TILE_SIZE;
        game.setGameCameraEnemy(setGameCameraEnemy());


        game.getOtherPlayer().setxPos(game.getPlayer().getxPos() + Tile.TILE_SIZE);
        game.getOtherPlayer().setyPos(game.getPlayer().getyPos());
        game.getOtherPlayer().setTarget(new Point((int) game.getOtherPlayer().getxPos(), (int) game.getOtherPlayer().getyPos()));

        game.setWitch(new Witch());

        game.getWitch().setxPos(GingerbreadHouse.getInstance().getY() * Tile.TILE_SIZE + Tile.TILE_SIZE);
        game.getWitch().setyPos(GingerbreadHouse.getInstance().getX() * Tile.TILE_SIZE + 2 * Tile.TILE_SIZE);
        game.getWitch().setHomeX(game.getWitch().getxPos());
        game.getWitch().setHomeY(game.getWitch().getyPos());

        game.setAliceCooper(new AliceCooper());
        game.getListOfAllEntities().addAll(Arrays.asList(game.getPlayer(), game.getOtherPlayer(), game.getWitch()));

    }

    public void initObservers() {
        List<MapObject> mapObjects = game.getMap().getMapSector().getAllContainingMapObjects();
        for (MapObject mapObject : mapObjects) {
            mapObject.addObserver(this);
        }
    }

    protected GameCamera setGameCameraEnemy() {
        return new GameCamera(game.getMap().getSize(), game.getMap().getSize(), game.getOtherPlayer());
    }

    public NetworkController.NetworkRole getNetworkRole() {
        return null;
    }

    /**
     * after a the visit method of a house is called this method is notified by notifyObservers call.
     * this is usaly done (normal houses big or small) to  replace house tiles from unvisted to visited tiles.
     * One edge case on the other hand is the visit to the witch's house, which triggers the spawn of the key to release
     * children in the townhall and also replaces the tiles of that witch's house,.
     *
     * @param o   Observable Object which called notifyObservers method
     * @param arg not used
     */
    @Override
    public void update(Observable o, Object arg) {


        if (o instanceof GingerbreadHouse) {
            for (MapObject obj : game.getMap().getMapSector().getAllContainingMapObjects()) {
                if (obj instanceof TownHall) {
                    TownHall t = (TownHall) obj;
                    t.setHasKey(true);
                    t.repaintAfterVisit();
                    t.updateMap();
                    if (t.getNumberOfPlayerInside() > 0) {
                        game.getMap().getMap()[29][31][1].setTileNr(120);
                    }
                    break; // found townhall no further looping necessary
                }
            }
            /**
             * the object is the GingerbreadHouse singleton so we use it instead of obj variable
             */
            GingerbreadHouse.getInstance().repaintAfterVisit();
            GingerbreadHouse.getInstance().updateMap();
        } else if (o instanceof House) {
            House h = (House) o;
            h.repaintAfterVisit();
            h.updateMap();
        }

    }

    public void startGameLoop() {
        this.gameLoop.start();
    }

    private class GameLoop extends AnimationTimer {
        @Override
        public void handle(long now) {
            long startTime = System.currentTimeMillis();
            game.ticks++;
            game.update();
            game.getMapRenderer().render();
            calculateGameTime(startTime);
        }

        /**
         * calculate and update current game time - sleep a few milliseconds when finished work before time goal
         *
         * @param startTime
         */
        public void calculateGameTime(long startTime) {
            if (!game.paused) {
                long endTime = System.currentTimeMillis();
                try {
                    int sleepTime = (int) (1000 / Game.FRAMES - (endTime - startTime));
                    if (sleepTime < 0) sleepTime = 0;
                    Thread.sleep(sleepTime);

                    int gameTime = (int) (game.getGameTime() - (System.currentTimeMillis() - startTime));
                    if (gameTime > 0) {
                        game.setGameTime((int) (game.getGameTime() - (System.currentTimeMillis() - startTime)));

                        /** set game as DRAMATIC (enables dramatic music and witch movement)
                         */
                        if (game.getGameTime() < 30000 && !Game.DRAMATIC) {
                            Game.DRAMATIC = true;
                            Sound.playCountdown();
                        }
                    }
                    /**
                     * stop the GameLoop when time is over -> show GameOver menu
                     */
                    else {
                        this.stop();
                        new GameOver(game, gameLauncher, game.getStage(), gameLauncher.getMainMenu()).showGameOver();
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void shutDownNetwork() {
        if (game.gameMode == Game.GameMode.REMOTE) {
            if (game.gameController.getNetworkRole() == NetworkController.NetworkRole.SERVER) {
                ServerEngine serverEngine = ((ServerEngine) ((NetworkController) game.gameController).getNetworkEngine());
                serverEngine.stopHandler();
                try {
                    if (serverEngine.serverSocket != null)
                        serverEngine.serverSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                serverEngine = null;


            } else if (game.gameController.getNetworkRole() == NetworkController.NetworkRole.CLIENT) {
                ClientEngine clientEngine = ((ClientEngine) ((NetworkController) game.gameController).getNetworkEngine());
                clientEngine.interrupt();
                clientEngine = null;
            }
        }
    }
}
