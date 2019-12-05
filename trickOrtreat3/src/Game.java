import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

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

        // NUR TESTCODE - WIRD IN EINE EXTRA-KLASSE AUSGELAGERT
        window.getScene().setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                System.out.println(gameCamera.getXOffset());
                switch (event.getCode()) {
                    case UP:    player.setyPos(player.getyPos() - 64); break;
                    case DOWN:  player.setyPos(player.getyPos() + 64); break;
                    case LEFT:  player.setxPos(player.getxPos() - 64); break;
                    case RIGHT:  player.setxPos(player.getxPos() + 64); break;
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

    }

    public Game(int size){
        this.map = new Map(size);
        this.gameMode = GameMode.LOCAL;
    }

    public void startGame(){
    }

    public void update(){
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