import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {

    private Map map;
    private Game game;

    private Stage stage;
    private Group root;
    private Scene scene;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 768;


    public Stage getStage() {
        return stage;
    }

    //private Stage stage;

    public Window(Game game, Stage stage,  Map map){
        this.map = map;
        this.game = game;
        this.stage = stage;
        //stage.setResizable(false);
    }


    public void showGUI(){
        this.root = new Group();
        this.scene = new Scene(root, WIDTH, HEIGHT);
        stage.setScene(scene);
        stage.show();
        //stage.setScene(scene);

    }

    public Game getGame() {
        return game;
    }

    public Group getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
