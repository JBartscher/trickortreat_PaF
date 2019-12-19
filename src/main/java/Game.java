package main.java;

import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.House;
import main.java.map.*;

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

    private GameLauncher launcher;

    // constructor with test map size
    public Game(GameLauncher launcher, Stage stage, GameMode gameMode) {
        this.launcher = launcher;
        map = new Map(40);
        generator = new MapGenerator(map);
        generator.createMap();
        this.gameMode = gameMode;

        // NUR TESTCODE FÜR DAS RENDERN DER PLAYEROBJEKTE
        this.player = new Player();
        this.otherPlayer = new Player();
        otherPlayer.setxPos(Tile.TILE_SIZE * 5);
        otherPlayer.setyPos(Tile.TILE_SIZE * 5);
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

        // NUR TESTCODE - WIRD IN EINE EXTRA-KLASSE AUSGELAGERT
        window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                switch (event.getCode()) {

                case UP:
                    moveVertical(-Player.SPEED, player);
                    break;
                    case DOWN:
                        moveVertical(Player.SPEED, player);
                        break;
                    case LEFT:
                        moveHorizontal(-Player.SPEED, player);
                        break;
                    case RIGHT:
                        moveHorizontal(Player.SPEED, player);
                        break;
                    case W:
                        moveVertical(-Player.SPEED, otherPlayer);
                        break;
                    case S:
                        moveVertical(Player.SPEED, otherPlayer);
                        break;
                    case A:
                        moveHorizontal(-Player.SPEED, otherPlayer);
                        break;
                    case D:
                        moveHorizontal(Player.SPEED, otherPlayer);
                        break;
                }
            }
        });
        try {
            Sound.playMusic();
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    public Game(int size) {
        this.map = new Map(size);
        this.gameMode = GameMode.LOCAL;
    }

    /**
     * moves the player vertical
     *
     * @param range
     */
    private void moveVertical(int range, Player player) {
        player.setyPos(player.getyPos() + range);

        // check out of bounds
        if (outOfBounds(player)) {
            // revert movement
            player.setyPos(player.getyPos() - range);
            return;
        }

        Placeable p = new Placeable(player.getEntityPos().y, player.getEntityPos().x, 1, 1, 0);

        if (map.getMapSector().intersectsWithContainingItems(p)) {
            // collision with door
            if (map.getMap()[player.getEntityPos().y][player.getEntityPos().x].isDoorTile()) {

                System.out.println("COLLIDE WITH DOOR!");

                for (MapObject obj : map.getMapSector().getAllContainingMapObjects()) {
                    try {
                        House h = (House) obj;
                        if (h.intersects(p)) {
                            h.visit(player);
                        }
                    } catch (ClassCastException ex) {
                        // the Object is not a House
                        continue;
                    }
                }
            }
            System.out.println("COLLIDE!");
            // revert movement
            player.setyPos(player.getyPos() - range);
        }
    }

    /**
     * moves the player horizontal
     *
     * @param range
     */
    private void moveHorizontal(int range, Player player) {
        player.setxPos(player.getxPos() + range);
        // check out of bounds
        if (outOfBounds(player)) {
            // revert movement
            player.setxPos(player.getxPos() - range);
            return;
        }


        Placeable p = new Placeable(player.getEntityPos().y, player.getEntityPos().x, 1, 1, 0);

        if (map.getMapSector().intersectsWithContainingItems(p)) {
            System.out.println("COLLIDE!");
            // revert movement
            player.setxPos(player.getxPos() - range);
        }
    }

    /**
     * checks if the new player position would be out of bounds
     *
     * @return true if out of bounds failing which false
     */
    private boolean outOfBounds(Player player) {
        Placeable p = new Placeable(player.getEntityPos().y, player.getEntityPos().x, 1, 1, 0);
        // mapSector does not contain player anymore
        return !map.getMapSector().intersects(p);
    }

    public void update() {

        gameCamera.centerOnPlayer();
        if(gameMode == GameMode.LOCAL) {
            gameCameraEnemy.centerOnPlayer();
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