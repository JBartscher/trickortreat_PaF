package main.java;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class
GameLauncher extends Application {

    private Game game;
    public final static int FRAMES = 50;
    private Stage stage;
    private GameLoop gameLoop;

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
        gameLoop = new GameLoop();
        gameLoop.start();
    }

    private class GameLoop extends AnimationTimer{
        @Override
        public void handle(long now) {
            long startTime = System.currentTimeMillis();
            game.update();
            game.getMapRenderer().drawMap();
            long endTime = System.currentTimeMillis();
            // System.out.println("NEEDED TIME :" + (endTime - startTime));

            try {
                int sleepTime = (int)(1000 / FRAMES - (endTime - startTime));
                if(sleepTime < 0) sleepTime = 0;
                Thread.sleep(sleepTime);

                int gameTime = (int) (game.getGameTime() - (System.currentTimeMillis() - startTime));
                if(gameTime > 0)
                    game.setGameTime((int)(game.getGameTime() - (System.currentTimeMillis() - startTime)));
                else {
                    showGameOver();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public void showGameOver() {

        this.gameLoop.stop();
        this.gameLoop = null;

        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        Scene scene = new Scene(root, Window.WIDTH, Window.HEIGHT);
        Label labelScore = new Label ("Spieler 1 : " + game.getPlayer().getCandy() + " - Spieler 2 " +
                game.getOtherPlayer().getCandy()
                );
        Button buttonRestart = new Button("Neustarten");
        Button buttonMainMenu = new Button("Zurück zum Hauptmenü");

        buttonRestart.setOnAction( (e) -> {
           startGame(game.gameMode);
        });

        buttonMainMenu.setOnAction( (e) -> {
           showMainMenu();
        });

        root.getChildren().addAll(labelScore, buttonRestart, buttonMainMenu);
        stage.setScene(scene);

    }

    public static void main(String[] args){
        launch();
    }
}
