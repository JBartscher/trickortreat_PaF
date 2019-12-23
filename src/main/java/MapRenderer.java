package main.java;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.Tile;

import java.util.concurrent.TimeUnit;

public class MapRenderer {

    private Map map;
    private Window window;
    private Tile[][] tileMap;
    private Game game;

    public MapRenderer(Map map, Window window, Game game){
        this.map = map;
        this.tileMap = map.getMap();
        this.window = window;
        this.game = game;
        GraphicsUtility.initGraphics();
    }

    public void drawMap() {

        Group root = window.getRoot();
        root.getChildren().clear();
        GameCamera gameCamera = game.getGameCamera();
        Player player =  game.getPlayer();
        Player otherPlayer;


        // Erweiterungsmöglichkeit für lokalen Mehrspielermodus
        // Liste enthält 1 Objekt, sofern REMOTE - 2 Objekte wenn LOKAL => rendert 2 Karten
        // widthOffset verschiebt den Viewport für den zweiten Spieler
        int widthOffset = 0;
        for(Player obj : game.getListOfPlayers()){

            if(widthOffset > 0) {
                Line line = new Line();
                line.setStartX(Game.WIDTH);
                line.setStartY(0);
                line.setEndX(Game.WIDTH);
                line.setEndY(Window.HEIGHT);
                root.getChildren().add(line);

                // Lokale GameCamera austauschen (Rendern des zweiten Screens)
                gameCamera = game.getGameCameraEnemy();
                }

            // Karte rendern - verschieben, sofern Spieler 2 (LOKAL)
            for (int y = 0; y < tileMap.length; y++) {
                for (int x = 0; x < tileMap[y].length; x++) {
                    int xPos = x * Tile.TILE_SIZE - gameCamera.getXOffset() + widthOffset;
                    int yPos = (int)(y * Tile.TILE_SIZE - gameCamera.getYOffset() + Window.HEIGHT * 0.1);
                    // nur Zeichnen, wenn sichtbar - sonst verwerfen

                    if(yPos > -Tile.TILE_SIZE && yPos < Game.HEIGHT + Tile.TILE_SIZE * 2 && xPos > -Tile.TILE_SIZE + widthOffset && xPos < Game.WIDTH + widthOffset) {
                        ImageView imagePlayer = new ImageView(GraphicsUtility.getTileImage(tileMap[y][x].getTileNr()));
                        imagePlayer.setX(xPos);
                        imagePlayer.setY(yPos);
                        root.getChildren().add(imagePlayer);
                    }
                }
            }

            // Eigenen Spieler zeichnen
            ImageView imagePlayer = new ImageView(obj.getEntityImage());
            double xPos = obj.getxPos() - gameCamera.getXOffset() + widthOffset;
            double yPos = obj.getyPos() - gameCamera.getYOffset() + Window.HEIGHT * 0.1;
            imagePlayer.setX(xPos);
            imagePlayer.setY(yPos);
            root.getChildren().add(imagePlayer);


            // zeichnet jeweils den anderen Spieler, sofern lokaler Mulitplayer gespielt wird
            if(widthOffset == 0) {
                otherPlayer = game.getOtherPlayer();
            } else {
                otherPlayer = game.getPlayer();
            }
                xPos = otherPlayer.getxPos() - gameCamera.getXOffset() + widthOffset;
                yPos = otherPlayer.getyPos() - gameCamera.getYOffset() + Window.HEIGHT * 0.1;

                if (yPos > -Tile.TILE_SIZE && yPos < Game.HEIGHT + Tile.TILE_SIZE && xPos > -Tile.TILE_SIZE + widthOffset && xPos < Game.WIDTH + widthOffset) {
                    ImageView otherPlayerObject = new ImageView(otherPlayer.getEntityImage());
                    otherPlayerObject.setX(xPos);
                    otherPlayerObject.setY(yPos);
                    root.getChildren().add(otherPlayerObject);

                }
            widthOffset += Game.WIDTH;
        }


        // HEADLINE
        Image head = new Image(MapRenderer.class.getResourceAsStream("headline.png"));
        ImageView headLine = new ImageView(head);
        root.getChildren().add(headLine);

        // Zeitanzeige
        Text text = new Text();
        text.setStyle("-fx-font: 32 arial;");
        text.setFill(Color.WHITE);
        text.setX(Window.WIDTH / 2 - 10);
        text.setY(50.0);


        Text textCandy = new Text("Spieler 1 - Candy: " + String.valueOf(game.getPlayer().getCandy()));
        textCandy.setStyle("-fx-font: 32 arial;");
        textCandy.setFill(Color.WHITE);
        textCandy.setX(100);
        textCandy.setY(50.0);

        ImageView imageCandyPlayer  = new ImageView(GraphicsUtility.getCandyImage());
        imageCandyPlayer.setX(Window.WIDTH / 3 - 30);
        imageCandyPlayer.setY(22);

        Text textCandy2 = new Text("Spieler 2 - Candy: " + String.valueOf(game.getOtherPlayer().getCandy()));
        textCandy2.setStyle("-fx-font: 32 arial;");
        textCandy2.setFill(Color.WHITE);
        textCandy2.setX(850);
        textCandy2.setY(50.0);

        ImageView imageCandyPlayer2  = new ImageView(GraphicsUtility.getCandyImage());
        imageCandyPlayer2.setX(1150);
        imageCandyPlayer2.setY(22);


        int gameTime = game.getGameTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(gameTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(gameTime - minutes * 1000 * 60);
        String secondsString = String.valueOf(seconds);
        if(seconds < 10) { secondsString = "0" + seconds; }
        text.setText(minutes + ":" + secondsString);


        text.setStrokeWidth(5.0);
        root.getChildren().addAll(text, textCandy, imageCandyPlayer, textCandy2, imageCandyPlayer2);
    }


    public void renderMapAndPlayer() {


    }

}