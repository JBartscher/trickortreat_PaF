package main.java;

import javafx.scene.Group;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.TilePane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.map.Map;
import main.java.map.Tile;

import java.awt.image.TileObserver;

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

        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                /*
                Rectangle rect = new Rectangle(y * Tile.TILE_SIZE - game.getGameCamera().getXOffset(), x * Tile.TILE_SIZE - game.getGameCamera().getYOffset(),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
                */
                int xPos = x * Tile.TILE_SIZE - game.getGameCamera().getXOffset();
                int yPos = y * Tile.TILE_SIZE - game.getGameCamera().getYOffset();

                // nur Zeichnen, wenn sichtbar - sonst verwerfen
                if(yPos > -Tile.TILE_SIZE && yPos < Window.HEIGHT && xPos > -Tile.TILE_SIZE && xPos < Window.WIDTH) {
                    ImageView rect = new ImageView(GraphicsUtility.getTileImage(tileMap[y][x].getTileNr()));

                    rect.setX(xPos);
                    rect.setY(yPos);
                    root.getChildren().add(rect);
                }


                //rect.setFill(tileMap[x][y].getTileColor());
                //root.getChildren().add(rect);
            }
        }
        // Erweiterungsmöglichkeit für Mehrspielermodus, abhängig jeder GameCamera wird der jeweilige Gegner gezeichnet
        for(Entity obj : game.getListOfEntities()){
            //Rectangle rect = new Rectangle(obj.getxPos() - game.getGameCamera().getXOffset(), obj.getyPos() - game.getGameCamera().getYOffset(), obj.getSize(), obj.getSize());
            Image img = new Image(MapRenderer.class.getResourceAsStream("player.png"));
            Image tileImage =  new WritableImage(img.getPixelReader(), 64, 0, Tile.TILE_SIZE, Tile.TILE_SIZE);
            ImageView rect = new ImageView(tileImage);
            double xPos = obj.getxPos() - game.getGameCamera().getXOffset();
            double yPos = obj.getyPos() - game.getGameCamera().getYOffset();
            rect.setX(xPos);
            rect.setY(yPos);

            root.getChildren().add(rect);
        }
    }

}