import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.stage.Stage;

public class GameLauncher extends Application {

    private Game game;
    GameLoop gameLoop = new GameLoop();

    public void start(Stage stage) throws Exception {
        this.game = new Game(this, stage);
        //game.startGame();

        GameLoop gameLoop = new GameLoop();
        gameLoop.handle(1000 / Game.FRAMES);
    }

    private class GameLoop extends AnimationTimer{
        @Override
        public void handle(long now) {
                long startTime = System.currentTimeMillis();
                game.getMapRenderer().drawMap();
                long endTime = System.currentTimeMillis();


        }
    }

    public static void main(String[] args){
        launch();
    }

}
