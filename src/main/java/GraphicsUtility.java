package main.java;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import main.java.map.Tile;


// Constructor of MapRenderer calls initGraphics
public class GraphicsUtility {


    // tileset 12x12 => 144 tiles
    public static Image[] tileImages = new Image[144];

    public static void initGraphics(){

        initTileImages();
    }

    public static void initTileImages()
    {
        Image image = new Image(GraphicsUtility.class.getResourceAsStream("tileset.png"));

        for(int y = 0; y < 12; y++){
            for(int x = 0; x < 12; x++){
                tileImages[y * 12 + x] = new WritableImage(image.getPixelReader(), x * (Tile.TILE_SIZE + 3), y * (Tile.TILE_SIZE + 3), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }

    public static Image getTileImage(int nr) {

        /*
        if (nr >= 0 && nr < 144){
            return tileImages[nr];
        } else {
            return null;
        }
         */

        if(nr == 1) { return tileImages[22]; }
        if(nr == 5 || nr == 6 || nr == 7 ) { return tileImages[13]; }
        if(nr == 8) { return tileImages[47]; }
        else { return tileImages[nr];  }


    }
}
