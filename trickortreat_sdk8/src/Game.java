import javafx.stage.Stage;

public class Game {
    public static final int FRAMES = 50;
    public int width = 960;
    public int height = 640;
    private GameMode gameMode;

    public enum GameMode {
        LOCAL, REMOTE
    }

    private Map map;
    private MapGenerator generator;
    private Player player;
    private Player otherPlayer;

    private Window window;
    private GameCamera camera;
    public MapRenderer getMapRenderer() {
        return mapRenderer;
    }

    private MapRenderer mapRenderer;
    private GameLauncher launcher;

    // constructor with test map size
    public Game(GameLauncher launcher, Stage stage) {
        this.launcher = launcher;
        this.map = new Map(40);
        this.generator = new MapGenerator(map);
        generator.createMap();
        this.gameMode = GameMode.LOCAL;

        window = new Window(this, stage, map);
        window.showGUI();

        mapRenderer = new MapRenderer(map, window);
    }

    public Game(int size){
        this.map = new Map(size);
        this.gameMode = GameMode.LOCAL;
    }

    public void startGame(){

    }




}