package main.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class
GameLauncher extends Application {

    private Game game;
    public final static int FRAMES = 50;
    private Stage stage;

    public void start(Stage stage) throws Exception {
        this.stage = stage;
        showMainMenu();
    }

    public void showMainMenu(){
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);

        Label label = new Label("Main Menu");
        Button buttonLocal = new Button("LOKALES MULTIPLAYERSPIEL STARTEN");
        Button buttonRemote = new Button("NETZWERKSPIEL STARTEN");
        root.getChildren().addAll(label, buttonLocal, buttonRemote);

        buttonLocal.setOnAction( (e) -> {
            startGame(Game.GameMode.LOCAL);
        });

        buttonRemote.setOnAction( (e) -> {
            startGame(Game.GameMode.REMOTE);
        });

        stage.setScene(new Scene(root, Window.WIDTH, Window.HEIGHT));
        stage.show();
    }

    public void startGame(Game.GameMode gameMode) {
        this.game = new Game(this, stage, gameMode);
        GameLoop gameLoop = new GameLoop();
        gameLoop.start();
    }

    private class GameLoop extends AnimationTimer{
        @Override
        public void handle(long now) {
            long startTime = System.currentTimeMillis();
            game.update();
            game.getMapRenderer().drawMap();
            long endTime = System.currentTimeMillis();
            System.out.println("NEEDED TIME :" + (endTime - startTime));

            try {
                int sleepTime = (int)(1000 / FRAMES - (endTime - startTime));
                if(sleepTime < 0) sleepTime = 0;
                Thread.sleep(sleepTime);
                game.setGameTime((int)(game.getGameTime() - (System.currentTimeMillis() - startTime)));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public static void main(String[] args){
        launch();
    }
}
