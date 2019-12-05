package main.java;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainMenu {


    public static void showMainMenu(Stage stage) {

        VBox root = new VBox();
        Button buttonStart = new Button("Start a new Game");
        //root.getChildren().add(buttonStart);

        Scene sceneMainMenu = new Scene(root, Window.WIDTH, Window.HEIGHT);
        stage.setScene(sceneMainMenu);
        stage.show();
    }
}
