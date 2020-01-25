package main.java;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.network.ClientEngine;
import main.java.network.NetworkController;
import main.java.network.ServerEngine;

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

        closeEvent();

    }

    private void closeEvent() {

        stage.setOnHiding( event -> {
            game.getGameController().shutDownNetwork();
            Platform.exit();
        });
    }

    public void createGUI() {
        this.root = new Group();
        this.scene = new Scene(root, WIDTH, HEIGHT);
    }

    /**
     * wird aufgerufen wenn das eigentliche Spiel gestartet wird
     */
    public void showGameGUI() {
        stage.show();
        if(game.gameMode == Game.GameMode.REMOTE) {
            if(game.getGameController().getNetworkRole() == NetworkController.NetworkRole.CLIENT) {
                stage.setTitle("Trick or Treat - CLIENT");
            } else {
                stage.setTitle("Trick or Treat - SERVER");
            }
        }
        stage.setScene(scene);
    }

    public Group getRoot() {
        return root;
    }

    public Scene getScene() {
        return scene;
    }
}
