package main.java;

import javafx.stage.Stage;
import main.java.Menu.GameMenu;
import main.java.Network.GameStateInit;
import main.java.Network.Network;
import main.java.Network.NetworkController;
import main.java.gameobjects.AliceCooper;
import main.java.gameobjects.Entity;
import main.java.gameobjects.Player;
import main.java.gameobjects.Witch;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.map.Map;
import main.java.map.MapGenerator;
import main.java.map.MapObject;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * this class represents the game and get called by the super controller "gameLoop" within the GameLauncher-Class
 * all model, view and controllers are encapsulated within the Game-Class
 */
public class Game {

    private final static Configuration<Object> config = new Configuration<Object>();
    public final static int FRAMES = ((Number) config.getParam("frames")).intValue();
    public final static int TIME = ((Number) config.getParam("time")).intValue();
    public int gameTime = TIME;
    public static int WIDTH = Window.WIDTH;
    public static int HEIGHT = (int)(Window.HEIGHT * 0.9);
    public static boolean DRAMATIC = false;

    private Map map;

    public Player player;
    public Player otherPlayer;

    /**
     * contains a list of player which get rendered on this computer
     * in remote games there is only 1 player in the list - in locale : 2
     */

    private CopyOnWriteArrayList<Player> listOfPlayers = new CopyOnWriteArrayList<>();
    private Witch witch;
    private AliceCooper aliceCooper;

    /**
     * contains a list of all entities: player1, player2 and the NPC
     * is used to determine if collisions between entities occurred
     */
    private CopyOnWriteArrayList<Entity> listOfAllEntities = new CopyOnWriteArrayList<>();
    private Stage stage;
    private Window window;

    private GameCamera gameCamera;
    private GameCamera gameCameraEnemy;
    private MapRenderer mapRenderer;

    private MovementManager movementManager;

    private GameLauncher launcher;

    /**
     * each iteration of the gameLoop is one tick (currently 20 ms)
     * a tick is used to give effects or events a certain amount of time
     */
    public int ticks = 0;
    public boolean paused;

    /**
     * network and multiplayer
     */
    public enum GameMode {
        LOCAL, REMOTE
    }

    public GameMode gameMode;
    public GameController gameController;

    /** main constructor when playing locale or host a game
     *  networkEngine is only used in gameMode Remote otherwise the reference is null and not used
      */
    public Game(GameLauncher launcher, Stage stage, GameMode gameMode, Network networkEngine, MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {
        Game.DRAMATIC = false;
        this.launcher = launcher;
        map = new Map(60);
        MapGenerator generator = new MapGenerator(map);
        generator.createMap();
        this.gameMode = gameMode;

        if(gameMode == GameMode.REMOTE) {
            gameController = new NetworkController(this, networkEngine, NetworkController.NetworkRole.SERVER, launcher);
        } else {
            gameController = new GameController(this, launcher);
        }

        /**
         * initialize the data of entitites, graphics and sound
         * also add observers to observables
         */
        gameController.initEntities(movementTypePlayer1, movementTypePlayer2);
        gameController.initGUIandSound(stage);
        gameController.initObservers();
    }

    /**
     * this constructor is used when playing in a network game as a client
     * the client gets a gamestate from the server and use this to create their own game
     */
    public Game(Network networkEngine, GameStateInit gameState, Stage stage, MovementManager.MovementType movementType, GameLauncher gameLauncher) {
        Game.DRAMATIC = false;
        this.gameController = new NetworkController(this, networkEngine, NetworkController.NetworkRole.CLIENT, gameLauncher);
        this.launcher = gameLauncher;
        ((NetworkController)gameController).updateGameState(gameState);
        this.gameMode = GameMode.REMOTE;

        this.player = new Player(movementType);
        this.otherPlayer = new Player(null);

        this.player.setGameStateData(gameState.getPlayerData());
        this.otherPlayer.setGameStateData(gameState.getOtherPlayerData());

        /**
         * add observers
         */
        player.addObserver(GameMenu.getInstance().getSecondPlayerObserver());
        otherPlayer.addObserver(GameMenu.getInstance().getFirstPlayerObserver());

        this.aliceCooper = new AliceCooper();
        this.witch = new Witch();
        this.aliceCooper.setGameStateData(gameState.getCooperData());
        this.witch.setGameStateData(gameState.getWitchData());

        this.listOfPlayers.add(player);

        this.listOfAllEntities.addAll(Arrays.asList(player, otherPlayer, witch));
        this.map = gameState.getMap();

        for(MapObject o : map.getMapSector().getAllContainingMapObjects()) {
            if(o instanceof GingerbreadHouse) {
                GingerbreadHouse.setInstance((GingerbreadHouse)o);
            }
        }

        gameController.initGUIandSound(stage);
        gameController.initObservers();
    }

    /**
     * update game data like game camera and animation images
     */
    public void update() {
        if(paused) return;
        checkGameOver();
        updateProtection();
        movementManager.moveAllEntities(gameController, listOfPlayers, witch);

        /**
         * show animation image of other entity
         */
        if(gameMode == GameMode.REMOTE) {
            otherPlayer.setEntityImage(true);
            //TODO: TESTEN!
            witch.setEntityImage(true);
        }

        gameCamera.centerOnPlayer();
        if(gameMode == GameMode.LOCAL) {
            gameCameraEnemy.centerOnPlayer();
        }
    }

    /**
     * check if both player have no children left -> show GameOver if that is the case
     */
    public void checkGameOver() {

        if(player.getChildrenCount() == 0 && otherPlayer.getChildrenCount() == 0) {
            gameTime = 0;
        }
    }

    /**
     * after a collision with the witch or when entering buildings, the player is protected for a certain amount of time
     * in this time period no collisions or enterings are possible
     */
    public void updateProtection() {

        if(player.getChildrenCount() <= 0) {
            listOfAllEntities.remove(player);
        }

        if(otherPlayer.getChildrenCount() <= 0) {
            listOfAllEntities.remove(otherPlayer);
        }

        for(Player player : listOfPlayers) {
            if(player.getProtectedTicks() > 0) {
                player.setProtectedTicks(player.getProtectedTicks() - 1);
            }
        }
    }

    /**
     * Getter and Setter-methods
     * @return
     */
    public Player getPlayer() { return player; }

    public Player getOtherPlayer() { return otherPlayer; }

    public Witch getWitch() { return witch; }

    public void setWitch(Witch witch) { this.witch = witch; }

    public AliceCooper getAliceCooper() { return aliceCooper; }

    public int getGameTime() { return gameTime; }

    public void setGameTime(int gameTime) { this.gameTime = gameTime; }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public GameCamera getGameCameraEnemy() {
        return gameCameraEnemy;
    }

    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public CopyOnWriteArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public Map getMap() { return map; }

    public void setMap(Map map) { this.map = map; }

    public Window getWindow() {
        return window;
    }

    public GameMode getGameMode() { return gameMode;  }

    public CopyOnWriteArrayList<Entity> getListOfAllEntities() {
        return listOfAllEntities;
    }

    public void setWindow(Window window) {
        this.window = window;
    }

    public void setGameCamera(GameCamera gameCamera) {
        this.gameCamera = gameCamera;
    }

    public void setMapRenderer(MapRenderer mapRenderer) {
        this.mapRenderer = mapRenderer;
    }

    public void setMovementManager(MovementManager movementManager) {
        this.movementManager = movementManager;
    }

    public MovementManager getMovementManager() {
        return movementManager;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setOtherPlayer(Player otherPlayer) {
        this.otherPlayer = otherPlayer;
    }

    public void setAliceCooper(AliceCooper aliceCooper) {
        this.aliceCooper = aliceCooper;
    }

    public void setGameCameraEnemy(GameCamera gameCameraEnemy) {
        this.gameCameraEnemy = gameCameraEnemy;
    }

    public GameController getGameController() {
        return gameController;
    }

    public Stage getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public GameLauncher getLauncher() {
        return launcher;
    }

}