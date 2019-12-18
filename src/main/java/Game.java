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
    public int width = 960;
    public int height = 640;
    public GameMode gameMode;

    // decide between LOCAL and REMOTE
    public enum GameMode {
        LOCAL, REMOTE
    }

    private Map map;
    private MapGenerator generator;
    public Player player;
    public Player otherPlayer;
    private ArrayList<Entity> listOfEntities = new ArrayList<>();

    private Window window;
    private GameCamera gameCamera;
    private MapRenderer mapRenderer;

    private GameLauncher launcher;

    // constructor with test map size
    public Game(GameLauncher launcher, Stage stage) {
        launcher = launcher;
        map = new Map(40);
        generator = new MapGenerator(map);
        generator.createMap();
        gameMode = GameMode.LOCAL;

        // GUI-Bereich
        window = new Window(this, stage, map);
        window.showGUI();
        mapRenderer = new MapRenderer(map, window, this);
        gameCamera = new GameCamera(map.getSize_x(), map.getSize_y());

        for(int y = 0; y < map.getMap().length; y++){
            for(int x = 0; x < map.getMap()[y].length; x++){
                System.out.print(map.getMap()[y][x].getTileNr() + " ");
            }
            System.out.println("");
        }


        // NUR TESTCODE - WIRD IN EINE EXTRA-KLASSE AUSGELAGERT
        window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {

                // player.getPlaceable().intersects(map.getMapSector().intersectsWithContainingItems())

                // System.out.println(gameCamera.getXOffset());
                switch (event.getCode()) {

                case UP:
                    moveVertical(-Player.SPEED);
                    // player.setyPos(player.getyPos() - Player.SPEED);
                    break;
                case DOWN:
                    moveVertical(Player.SPEED);
                    // player.setyPos(player.getyPos() + Player.SPEED);
                    break;
                case LEFT:
                    moveHorizontal(-Player.SPEED);
                    // player.setxPos(player.getxPos() - Player.SPEED);
                    break;
                case RIGHT:
                    moveHorizontal(Player.SPEED);
                    // player.setxPos(player.getxPos() + Player.SPEED);
                    break;
                }
            }
        });

        // NUR TESTCODE FÜR DAS RENDERN DER PLAYEROBJEKTE
        this.player = new Player();
        Player enemy = new Player();
        listOfEntities.add(player);
        listOfEntities.add(enemy);
        enemy.setxPos(Tile.TILE_SIZE * 5);
        enemy.setyPos(Tile.TILE_SIZE * 5);

        Sound.playMusic();
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
    private void moveVertical(int range) {
        player.setyPos(player.getyPos() + range);

        // check out of bounds
        if (outOfBounds()) {
            // revert movement
            player.setyPos(player.getyPos() - range);
            return;
        }

        Placeable p = new Placeable(player.getEntityPos().y, player.getEntityPos().x, 1, 1, 0);

        if (map.getMapSector().intersectsWithContainingItems(p)) {
            // collision with door
            if (map.getMap()[player.getEntityPos().y][player.getEntityPos().x].getTileNr() == 8) {

                System.out.println("COLLIDE WITH DOOR!");

                Sound.playRing();

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
    private void moveHorizontal(int range) {
        player.setxPos(player.getxPos() + range);
        // check out of bounds
        if (outOfBounds()) {
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
    private boolean outOfBounds() {
        Placeable p = new Placeable(player.getEntityPos().y, player.getEntityPos().x, 1, 1, 0);
        if (!map.getMapSector().intersects(p)) {
            // mapSector does not contain player anymore
            System.out.println("out");
            return true;
        } else {
            System.out.println("in");
            return false;
        }
    }

    public void startGame() {
    }

    public void update() {
        // System.out.println(player.getEntityPos());
        gameCamera.centerOnEntity(player);
    }

    public GameCamera getGameCamera() {
        return gameCamera;
    }

    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

    public ArrayList<Entity> getListOfEntities() {
        return listOfEntities;
    }

}