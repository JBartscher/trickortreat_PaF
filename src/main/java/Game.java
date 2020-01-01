package main.java;

import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import main.java.Network.Network;
import main.java.Network.NetworkController;
import main.java.Network.ServerEngine;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.MapGenerator;
import main.java.map.Tile;

import java.util.ArrayList;

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
    private ArrayList<Player> listOfPlayers = new ArrayList<>();

    private Window window;
    private GameCamera gameCamera;
    private GameCamera gameCameraEnemy;
    private MapRenderer mapRenderer;

    private MovementManager movementManager;

    private GameLauncher launcher;

    // Network and Multiplayer

    // decide between LOCAL and REMOTE
    public enum GameMode {
        LOCAL, REMOTE
    }

    public GameMode gameMode;

    public NetworkController networkController;

    // constructor with test map size
    // networkEngine is only used in gameMode Remote otherwise the reference is null and not used
    public Game(GameLauncher launcher, Stage stage, GameMode gameMode, Network networkEngine, MovementManager.MovementType movementTypePlayer1, MovementManager.MovementType movementTypePlayer2) {
        this.launcher = launcher;
        map = new Map(40);
        generator = new MapGenerator(map);
        generator.createMap();
        this.gameMode = gameMode;

        // NUR TESTCODE FÜR DAS RENDERN DER PLAYEROBJEKTE
        this.player = new Player(movementTypePlayer1);

        listOfPlayers.add(player);

        // Sofern LOKAL gespielt wird, gibt es eine zweite Kamera
        // Liste an zeichenbaren Objekten erweitert sich
        if(this.gameMode == GameMode.LOCAL) {
            this.otherPlayer = new Player(movementTypePlayer2);
            listOfPlayers.add(otherPlayer);
            gameCameraEnemy = new GameCamera(map.getSize(), map.getSize(), otherPlayer);
            Game.WIDTH = Window.WIDTH / 2 - Tile.TILE_SIZE;
        }
        // setzt die Rolle im Netzwerk auf Server
        // instanziert den NetworkController, der die Netzwerkeigenschaften bündelt
        else {
            this.networkController = new NetworkController(networkEngine, NetworkController.NetworkRole.SERVER);
            this.otherPlayer = new Player(null);
        }

        otherPlayer.setxPos(player.getxPos());
        otherPlayer.setyPos(player.getyPos() + Tile.TILE_SIZE);

        initGUIandSound(stage);

    }


    // get GameState from Server - get only called by CLIENT
    public Game(Network networkEngine, GameState gameState, Stage stage, MovementManager.MovementType movementType) {

        this.networkController = new NetworkController(networkEngine, NetworkController.NetworkRole.CLIENT);
        networkController.setGameState(gameState);
        this.gameMode = GameMode.REMOTE;

        this.player = new Player(movementType);
        this.otherPlayer = new Player(null);

        this.player.setGameStateData(gameState.getPlayerData());
        this.otherPlayer.setGameStateData(gameState.getOtherPlayerData());

        this.listOfPlayers.add(player);

        this.map = gameState.getMap();

        initGUIandSound(stage);
    }

    public void initGUIandSound(Stage stage) {
        // GUI-Bereich
        window = new Window(this, stage);
        window.createGUI();
        mapRenderer = new MapRenderer(map, window, this);
        gameCamera = new GameCamera(map.getSize(), map.getSize(), player);
        this.movementManager = new MovementManager(map, player, otherPlayer);

        // Movement - Weiterleiten an Controller-Klasse
        window.getScene().addEventHandler(InputEvent.ANY, movementManager);

        try {
            //Sound.playMusic();
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }

    }


    public void update() {

        moveAllEntities();
        if(gameMode == GameMode.REMOTE) {

            otherPlayer.setEntityImage(true);
            if(networkController.getNetworkRole() == NetworkController.NetworkRole.SERVER) {
                ServerEngine serverEngine = (ServerEngine)networkController.getNetworkEngine();
                //serverEngine.isWaiting = false;
            }
        }


        gameCamera.centerOnPlayer();
        if(gameMode == GameMode.LOCAL) {
            gameCameraEnemy.centerOnPlayer();
        }

    }

    public void moveAllEntities() {
        for(Player player : listOfPlayers)
        {
            movementManager.moveObject(player);
        }
    }

    public Player getPlayer() { return player; }

    public Player getOtherPlayer() { return otherPlayer; }

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

    public ArrayList<Player> getListOfPlayers() {
        return listOfPlayers;
    }

    public Map getMap() { return map; }

    public void setMap(Map map) { this.map = map; }

    public NetworkController getNetworkController() {
        return networkController;
    }

    public Window getWindow() {
        return window;
    }

    public GameMode getGameMode() { return gameMode;  }


}