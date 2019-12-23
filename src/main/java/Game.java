package main.java;

import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.MapGenerator;

import java.util.ArrayList;

public class Game {
    public static final int FRAMES = 50;
    public final int TIME = 180000;


    public int gameTime = TIME;
    public static int WIDTH = Window.WIDTH;
    public static int HEIGHT = (int)(Window.HEIGHT * 0.9);
    public GameMode gameMode;

    // decide between LOCAL and REMOTE
    public enum GameMode {
        LOCAL, REMOTE
    }

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

    // constructor with test map size
    public Game(GameLauncher launcher, Stage stage, GameMode gameMode) {
        this.launcher = launcher;
        map = new Map(40);
        generator = new MapGenerator(map);
        generator.createMap();
        this.gameMode = gameMode;

        // NUR TESTCODE FÜR DAS RENDERN DER PLAYEROBJEKTE
        this.player = new Player(MovementManager.MovementType.KEYBOARD_AWSD);
        this.otherPlayer = new Player(MovementManager.MovementType.KEYBOARD_ARROW);

        otherPlayer.setxPos(Game.WIDTH);
        otherPlayer.setyPos(Game.HEIGHT);
        listOfPlayers.add(player);

        // Sofern LOKAL gespielt wird, gibt es eine zweite Kamera
        // Liste an zeichenbaren Objekten erweitert sich
        if(gameMode == GameMode.LOCAL) {
            listOfPlayers.add(otherPlayer);
            gameCameraEnemy = new GameCamera(map.getSize(), map.getSize(), otherPlayer);
            Game.WIDTH /= 2;
        }


        // GUI-Bereich
        window = new Window(this, stage);
        window.showGUI();
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

    public Game(int size) {
        this.map = new Map(size);
        this.gameMode = GameMode.LOCAL;
    }


    public void update() {

        gameCamera.centerOnPlayer();
        if(gameMode == GameMode.LOCAL) {
            gameCameraEnemy.centerOnPlayer();
        }
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

}