package main.java;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Window {

    private Game game;

    private Stage stage;
    private Group root;
    private Scene scene;

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 768;


    public Window(Game game, Stage stage) {
        this.game = game;
        this.stage = stage;

    }


    public void createGUI(){
        this.root = new Group();
        this.scene = new Scene(root, WIDTH, HEIGHT);

    }

    public void showGameGUI() {
        stage.show();
        stage.setScene(scene);
    }

    public Group getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
