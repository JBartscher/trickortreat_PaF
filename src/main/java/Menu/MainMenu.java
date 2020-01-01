package main.java.Menu;

import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.util.Pair;
import main.java.Game;
import main.java.GameLauncher;
import main.java.MovementManager;
import main.java.Network.ClientEngine;
import main.java.Network.ServerEngine;
import main.java.Window;

import java.util.Arrays;
import java.util.List;

public class MainMenu {

    private Stage stage;
    private GameLauncher gameLauncher;
    private Game.GameMode gameMode;

    private static final int WIDTH = Window.WIDTH;
    private static final int HEIGHT = Window.HEIGHT;

    private List<Pair<String, Runnable>> menuData = Arrays.asList(
            new Pair<String, Runnable>("Play local", () -> {

                gameMode = Game.GameMode.LOCAL;
                gameLauncher.startGame(Game.GameMode.LOCAL, null, MovementManager.MovementType.KEYBOARD_AWSD, MovementManager.MovementType.KEYBOARD_ARROW);

            }),
            new Pair<String, Runnable>("Host a Game", () -> {
                gameMode = Game.GameMode.REMOTE;
                new ServerEngine(gameLauncher, stage);

            }),
            new Pair<String, Runnable>("Join a Game", () -> {
                gameMode = Game.GameMode.REMOTE;
                new ClientEngine(gameLauncher, stage);

            }),

            new Pair<String, Runnable>("Credits", () -> {}),
            new Pair<String, Runnable>("Exit to Desktop", Platform::exit)
    );


    public MainMenu(Stage stage, GameLauncher gameLauncher) {
        this.stage = stage;
        this.gameLauncher = gameLauncher;
    }



    private Pane root = new Pane();
    private VBox menuBox = new VBox(-5);
    private Line line;

    private Parent createContent() {
        addBackground();
        addTitle();

        double lineX = WIDTH / 2 - 100;
        double lineY = HEIGHT / 3 + 50;

        addLine(lineX, lineY);
        addMenu(lineX + 5, lineY + 5);

        startAnimation();

        return root;
    }

    private void addBackground() {
        ImageView imageView = new ImageView(new Image(getClass().getResource("main_menu_converted.png").toExternalForm()));
        imageView.setOpacity(0.9);
        ColorAdjust colorAdjust = new ColorAdjust();
        colorAdjust.setBrightness(-0.5);
        imageView.setEffect(colorAdjust);

        imageView.setFitWidth(WIDTH);
        imageView.setFitHeight(HEIGHT);

        root.getChildren().add(imageView);
    }

    private void addTitle() {
        MenuTitle title = new MenuTitle("TRICK OR TREAT", 48);
        title.setTranslateX(WIDTH / 2 - title.getTitleWidth() / 2);
        title.setTranslateY(HEIGHT / 4);

        root.getChildren().add(title);
    }

    private void addLine(double x, double y) {
        line = new Line(x, y, x, y + 300);
        line.setStrokeWidth(3);
        line.setStroke(Color.color(1, 1, 1, 0.75));
        line.setEffect(new DropShadow(5, Color.BLACK));
        line.setScaleY(0);

        root.getChildren().add(line);
    }

    private void startAnimation() {
        ScaleTransition st = new ScaleTransition(Duration.seconds(1), line);
        st.setToY(1);
        st.setOnFinished(e -> {

            for (int i = 0; i < menuBox.getChildren().size(); i++) {
                Node n = menuBox.getChildren().get(i);

                TranslateTransition tt = new TranslateTransition(Duration.seconds(1 + i * 0.15), n);
                tt.setToX(0);
                tt.setOnFinished(e2 -> n.setClip(null));
                tt.play();
            }
        });
        st.play();
    }

    private void addMenu(double x, double y) {
        menuBox.setTranslateX(x);
        menuBox.setTranslateY(y);
        menuData.forEach(data -> {
            MenuItem item = new MenuItem(data.getKey());
            item.setOnAction(data.getValue());
            item.setTranslateX(-300);

            Rectangle clip = new Rectangle(300, 30);
            clip.translateXProperty().bind(item.translateXProperty().negate());

            item.setClip(clip);

            menuBox.getChildren().addAll(item);
        });

        root.getChildren().add(menuBox);
    }

    public void showMainMenu() {

        Scene scene = new Scene(createContent());
        stage.setTitle("Trick or Treat");
        stage.setScene(scene);
        stage.show();
    }


}
