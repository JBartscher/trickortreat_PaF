package main.java;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import main.java.Network.NetworkController;
import main.java.gameobjects.Player;
import main.java.map.Map;
import main.java.map.Tile;

import java.util.concurrent.TimeUnit;

public class MapRenderer {

    private Map map;
    private Window window;
    private Tile[][][] tileMap;
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

            System.out.println("GAME_width:" + game.WIDTH + " offset: " + widthOffset + "- Anzahl Spieler: " + game.getListOfPlayers());

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

            // Karte rendern - verschieben in x Richtung, sofern Spieler 2 (LOKAL)
            for(int z = 0; z < 3; z++) {

                for (int y = 0; y < tileMap.length; y++) {
                    for (int x = 0; x < tileMap[y].length; x++) {
                        int xPos = x * Tile.TILE_SIZE - gameCamera.getXOffset() + widthOffset;
                        int yPos = (int) (y * Tile.TILE_SIZE - gameCamera.getYOffset() + Window.HEIGHT * 0.1);
                        // nur Zeichnen, wenn sichtbar - sonst verwerfen

                        if(tileMap[y][x][z].getTileNr() == 0) continue;

                        if (yPos > -Tile.TILE_SIZE && yPos < Game.HEIGHT + Tile.TILE_SIZE * 2 && xPos > -Tile.TILE_SIZE + widthOffset && xPos < Game.WIDTH + widthOffset) {

                            ImageView imageTile = new ImageView(GraphicsUtility.getTileImage(tileMap[y][x][z].getTileNr()));
                            GraphicsUtility.setImageProperties(imageTile, xPos, yPos);
                            if(obj.getChildrenCount() == 0) {

                                imageTile.setOpacity(0.7);
                            }
                            root.getChildren().add(imageTile);

                        }
                    }
                }
            }

            // Eigenen Spieler und Anhang zeichnen
            for(int i = 0; i < obj.getChildrenCount(); i++) {
                ImageView imagePlayer = new ImageView(obj.getEntityImage());

                double xPos = obj.getxPos() - gameCamera.getXOffset() + widthOffset;
                double yPos = obj.getyPos() - gameCamera.getYOffset() + Window.HEIGHT * 0.1;

                // Kinder verschieben
                if(i == 1) { xPos += 0.33 * Tile.TILE_SIZE; }
                if(i == 2) { yPos += 0.33 * Tile.TILE_SIZE; }

                // Auf 50 % der Größe skalieren ( jedes Kind ist somit halb so hoch und breit wie ein ganzes Tile
                imagePlayer.setScaleX(0.5);
                imagePlayer.setScaleY(0.5);
                imagePlayer.setX(xPos);
                imagePlayer.setY(yPos);
                root.getChildren().add(imagePlayer);
            }




            Rectangle middleTile = null;
            // zeichnet jeweils den anderen Spieler und seinen Anhang
            if(widthOffset == 0) {
                otherPlayer = game.getOtherPlayer();
            } else {
                otherPlayer = game.getPlayer();
                middleTile = new Rectangle(Game.WIDTH, Window.HEIGHT * 0.1, 2 *  Tile.TILE_SIZE, Window.HEIGHT);

            }

            for(int i = 0; i < otherPlayer.getChildrenCount(); i++) {
                double xPosOffset = 0;
                double yPosOffset = 0;
                if(i == 1) { xPosOffset = 0.33 * Tile.TILE_SIZE; }
                if(i == 2) { yPosOffset = 0.33 * Tile.TILE_SIZE; }
                if(i == 3) { xPosOffset = 0.33 * Tile.TILE_SIZE; yPosOffset = 0.33 * Tile.TILE_SIZE; }

                drawEntity(root, otherPlayer, gameCamera, widthOffset, xPosOffset, yPosOffset, 0.5);
            }

            drawEntity(root, game.getWitch(), gameCamera, widthOffset, 0, 0, 1);
            drawEntity(root, game.getAliceCooper(), gameCamera, widthOffset, 0, 0, 1);

            // Mittelstück wird hinzugefügt, sofern ein Splitscreen existiert
            if(middleTile != null) root.getChildren().add(middleTile);


            widthOffset += Game.WIDTH + 2 * Tile.TILE_SIZE;
        }

        if(game.paused) {
            Text textPaused = new Text("PAUSED");
            Rectangle rect = new Rectangle(0, 0, Window.WIDTH, Window.HEIGHT);
            rect.setOpacity(0.3);
            GraphicsUtility.setTextProperties(textPaused, "-fx-font: 128 arial;", Color.DARKRED, Window.WIDTH / 2 - 250, 400.0);
            root.getChildren().addAll(textPaused, rect);

        }

        setHeadlineIcons(root);
    }

    public void drawEntity(Group root, Entity entity, GameCamera gameCamera, int widthOffset, double xPosOffset, double yPosOffset, double scaleFactor){

        double xPos = entity.getxPos() - gameCamera.getXOffset() + widthOffset + xPosOffset;
        double yPos = entity.getyPos() - gameCamera.getYOffset() + Window.HEIGHT * 0.1 + yPosOffset;

        if (yPos > -Tile.TILE_SIZE && yPos < Game.HEIGHT + Tile.TILE_SIZE && xPos > -Tile.TILE_SIZE + widthOffset && xPos < Game.WIDTH + widthOffset) {
            ImageView entityImage = new ImageView(entity.getEntityImage());
            GraphicsUtility.setImageProperties(entityImage, xPos, yPos);

            if(entity instanceof Witch) {
                entityImage.setOpacity(0.5);
            }

            if(scaleFactor != 1.0) {
                entityImage.setScaleX(scaleFactor);
                entityImage.setScaleY(scaleFactor);
            }

            root.getChildren().add(entityImage);
        }



    }

    public void setHeadlineIcons(Group root) {

        // HEADLINE
        Image head = new Image(MapRenderer.class.getResourceAsStream("headline.png"));
        ImageView headLine = new ImageView(head);
        root.getChildren().add(headLine);

        // Zeitanzeige
        Text text = new Text();
        GraphicsUtility.setTextProperties(text, "-fx-font: 32 arial;", Color.WHITE, Window.WIDTH / 2 - 10, 50.0);

        // Zeitanzeige
        Text textSound = new Text("MUTE SOUND with KEY M");
        GraphicsUtility.setTextProperties(textSound, "-fx-font: 16 arial;", Color.WHITE, Window.WIDTH / 2 - 80, 70);

        textSound.setOnMouseClicked( (e) -> {
            Sound.muteSound();
        });

        Text textCandy;
        Text textCandy2;

        if(game.gameMode == Game.GameMode.REMOTE && game.getGameController().getNetworkRole() == NetworkController.NetworkRole.SERVER) {
            textCandy = new Text("Spieler 1 - Candy: " + game.getPlayer().getCandy());
            textCandy2 = new Text("Spieler 2 - Candy: " + game.getOtherPlayer().getCandy());

        } else if(game.gameMode == Game.GameMode.REMOTE && game.getGameController().getNetworkRole() == NetworkController.NetworkRole.CLIENT) {
            textCandy = new Text("Spieler 1 - Candy: " + game.getOtherPlayer().getCandy());
            textCandy2 = new Text("Spieler 2 - Candy: " + game.getPlayer().getCandy());
        } else {
            textCandy = new Text("Spieler 1 - Candy: " + game.getPlayer().getCandy());
            textCandy2 = new Text("Spieler 2 - Candy: " + game.getOtherPlayer().getCandy());
        }


        textCandy.setStyle("-fx-font: 32 arial;");
        GraphicsUtility.setTextProperties(textCandy, "-fx-font: 32 arial;", Color.WHITE, 150, 50.0 );

        textCandy2.setStyle("-fx-font: 32 arial;");
        GraphicsUtility.setTextProperties(textCandy2, "-fx-font: 32 arial;", Color.WHITE, 800, 50.0 );

        ImageView imageCandyPlayer  = new ImageView(GraphicsUtility.getCandyImage());
        GraphicsUtility.setImageProperties(imageCandyPlayer, Window.WIDTH / 3 + 40, 22);

        ImageView imageCandyPlayer2  = new ImageView(GraphicsUtility.getCandyImage());
        GraphicsUtility.setImageProperties(imageCandyPlayer2, 1120, 22);

        text.setText(calculateTime(game));

        text.setStrokeWidth(5.0);
        root.getChildren().addAll(text, textCandy, imageCandyPlayer, textCandy2, imageCandyPlayer2, textSound);


    }

    public static String calculateTime(Game game) {

        int gameTime = game.getGameTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(gameTime);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(gameTime - minutes * 1000 * 60);
        String secondsString = String.valueOf(seconds);
        if(seconds < 10) { secondsString = "0" + seconds; }

        return minutes + ":" + secondsString;
    }

    public Map getMap() {
        return map;
    }



}