import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class MapRenderer {

    private Map map;
    private Window window;
    private Tile[][] tileMap;

    public MapRenderer(Map map, Window window){
        this.map = map;
        this.tileMap = map.getMap();
        this.window = window;
    }

    public void drawMap() {
        Group root = window.getRoot();
        root.getChildren().clear();
        for(int y = 0; y < tileMap.length; y++){
            for(int x = 0; x < tileMap[y].length; x++){
                Rectangle rect = new Rectangle(y * Tile.TILE_SIZE, x * Tile.TILE_SIZE,
                        Tile.TILE_SIZE, Tile.TILE_SIZE);

                // HARDGECODET , nur zum test
                Color color = null;
                if(tileMap[y][x].tileNr == 1){
                    color = Color.GREEN;
                } else if (tileMap[y][x].tileNr == 5){
                    color = Color.DARKRED;
                } else if (tileMap[y][x].tileNr == 8){
                    color = Color.BROWN;
                }
                rect.setFill(color);
                root.getChildren().add(rect);
            }
        }


    }

}