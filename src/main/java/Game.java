package main.java;

import javafx.stage.Stage;
import main.java.Network.GameState;
import main.java.Network.Network;
import main.java.Network.NetworkController;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.MapGenerator;

import java.util.Arrays;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game {
    public static final int FRAMES = 50;
    public final static int TIME = 180000;
    public int gameTime = TIME;
    public static int WIDTH = Window.WIDTH;
    public static int HEIGHT = (int)(Window.HEIGHT * 0.9);

    private Map map;
    private MapGenerator generator;

    public Player player;
    public Player otherPlayer;


    /**
     * repräsentiert alle Objekte, die von EINER Spielinstanz verwaltet werde
     * LOKAL = Ein Spiel verwaltet 2 Spieler => Liste enthält Spieler 1 und Spieler 2
     * REMOTE = Jedes Spiel kümmert sich nur um seine EIGENEN Spieler - Liste enthält 1 Objekt
     *
     */

    private CopyOnWriteArrayList<Player> listOfPlayers = new CopyOnWriteArrayList<>();

    private Witch witch;
    private AliceCooper aliceCooper;

    // enthält die Liste ALLER Entitäten : Spieler 1 + Spieler 2 + Hexe , zukünftig noch Alice Cooper
    // wichtig zur Kollisionserkennung
    private CopyOnWriteArrayList<Entity> listOfAllEntites = new CopyOnWriteArrayList<>();

    private Window window;

    private GameCamera gameCamera;
    private GameCamera gameCameraEnemy;
    private MapRenderer mapRenderer;

    private MovementManager movementManager;

    private GameLauncher launcher;

    // Eine Iteration der GameLoop
    public int ticks = 0;
    public boolean paused;

    // Network and Multiplayer

    // decide between LOCAL and REMOTE
    public enum GameMode {
        LOCAL, REMOTE
    }

    public GameMode gameMode;

    //public NetworkController networkController;
    public GameController gameController;

    // constructor with test map size
    // networkEngine is only used in gameMode Remote otherwise the reference is null and not used
    public Game(GameLauncher launcher, Stage stage, GameMode gameMode, Network networkEngine, MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {
        this.launcher = launcher;
        map = new Map(60);
        generator = new MapGenerator(map);
        generator.createMap();
        this.gameMode = gameMode;

        if(gameMode == GameMode.REMOTE) {
            gameController = new NetworkController(this, networkEngine, NetworkController.NetworkRole.SERVER);
        } else {
            gameController = new GameController(this);
        }

        // instanziert die Entitäten, setzt die Steuerung und ggf. Netzwerk
        //initPlayerAndNetwork(networkEngine, movementTypePlayer1, movementTypePlayer2);
        gameController.initEntities(movementTypePlayer1, movementTypePlayer2);
        gameController.initNetwork();
        gameController.initGUIandSound(stage);
        gameController.initObservers();
    }

    // get GameState from Server - get only called by CLIENT
    public Game(Network networkEngine, GameState gameState, Stage stage, MovementManager.MovementType movementType) {

        this.gameController = new NetworkController(this, networkEngine, NetworkController.NetworkRole.CLIENT);
        ((NetworkController)gameController).updateGameState(gameState);
        this.gameMode = GameMode.REMOTE;

        this.player = new Player(movementType);
        this.otherPlayer = new Player(null);

        this.player.setGameStateData(gameState.getPlayerData());
        this.otherPlayer.setGameStateData(gameState.getOtherPlayerData());

        this.aliceCooper = new AliceCooper();
        this.witch = new Witch();
        this.aliceCooper.setGameStateData(gameState.getCooperData());
        this.witch.setGameStateData(gameState.getWitchData());

        this.listOfPlayers.add(player);

        this.listOfAllEntites.addAll(Arrays.asList(player, otherPlayer, /*aliceCooper, */witch));

        this.map = gameState.getMap();

        gameController.initGUIandSound(stage);
        gameController.initObservers();
    }

    public void update() {

        if(paused) return;

        checkGameOver();
        updateProtection();
        movementManager.moveAllEntites(gameController, listOfPlayers, witch);
        if(gameMode == GameMode.REMOTE) {

            // Das Animationsbild der anderen Entität wird gesetzt - true setzt für den Aufruf aus einem Netzwerk-Kontext
            otherPlayer.setEntityImage(true);
        }

        gameCamera.centerOnPlayer();
        if(gameMode == GameMode.LOCAL) {
            gameCameraEnemy.centerOnPlayer();
        }
    }

    public void checkGameOver() {

        if(player.getChildrenCount() == 0 && otherPlayer.getChildrenCount() == 0) {
            gameTime = 0;
        }
    }

    public void updateProtection() {

        if(player.getChildrenCount() <= 0) {
            //listOfPlayers.remove(player);
            listOfAllEntites.remove(player);
        }

        if(otherPlayer.getChildrenCount() <= 0) {
            //listOfPlayers.remove(otherPlayer);
            listOfAllEntites.remove(otherPlayer);
        }

        for(Player player : listOfPlayers) {
            if(player.getProtectedTicks() > 0) {
                player.setProtectedTicks(player.getProtectedTicks() - 1);
            }
        }
    }

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

    public CopyOnWriteArrayList<Entity> getListOfAllEntites() {
        return listOfAllEntites;
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

}