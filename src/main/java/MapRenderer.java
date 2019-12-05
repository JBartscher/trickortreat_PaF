package main.java;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import main.java.map.Map;
import main.java.map.Tile;

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
    }

    public void drawMap() {

        Group root = window.getRoot();
        root.getChildren().clear();

        for (int y = 0; y < tileMap.length; y++) {
            for (int x = 0; x < tileMap[y].length; x++) {
                Rectangle rect = new Rectangle(y * Tile.TILE_SIZE - game.getGameCamera().getXOffset(), x * Tile.TILE_SIZE - game.getGameCamera().getYOffset(),
                        Tile.TILE_SIZE, Tile.TILE_SIZE);

                // HARDGECODET , nur zum test
                Color color = null;
                if (tileMap[y][x].getTileNr() == 1) {
                    color = Color.GREEN;
                } else if (tileMap[y][x].getTileNr() == 5) {
                    color = Color.DARKRED;
                } else if (tileMap[y][x].getTileNr() == 8) {
                    color = Color.BROWN;
                }
                rect.setFill(color);
                root.getChildren().add(rect);
            }
        }
        // Erweiterungsmöglichkeit für Mehrspielermodus, abhängig jeder GameCamera wird der jeweilige Gegner gezeichnet
        for(Entity obj : game.getListOfEntities()){
            Rectangle rect = new Rectangle(obj.getxPos() - game.getGameCamera().getXOffset(), obj.getyPos() - game.getGameCamera().getYOffset(), obj.getSize(), obj.getSize());
            root.getChildren().add(rect);
        }


    }

}