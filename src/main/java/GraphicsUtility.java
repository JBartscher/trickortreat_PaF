package main.java;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import main.java.map.Tile;


// Constructor of MapRenderer calls initGraphics
public class GraphicsUtility {


    // tileset 12x12 => 144 tiles
    public static Image[] tileImages = new Image[14];

    public static void initGraphics(){

        initTileImages();
    }

    public static void initTileImages()
    {
        Image image = new Image(GraphicsUtility.class.getResourceAsStream("tileset.png"));

        for(int y = 0; y < 2; y++){
            for(int x = 0; x < 7; x++){
                tileImages[y * 7 + x] = new WritableImage(image.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
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

        if(nr == 1) { return tileImages[6]; }

        // SmallHOUSE

        if(nr == 51) { return tileImages[0]; }
        if(nr == 52) { return tileImages[1]; }
        if(nr == 53) { return tileImages[7]; }
        if(nr == 54) { return tileImages[8]; }

        // BigHouse
        if(nr == 61) { return tileImages[2]; }
        if(nr == 62) { return tileImages[3]; }
        if(nr == 63) { return tileImages[4]; }
        if(nr == 64) { return tileImages[9]; }
        if(nr == 65) { return tileImages[10]; }
        if(nr == 66) { return tileImages[11]; }

        //if(nr >= 51 && nr <= 66) {return tileImages[10]; }
        //if(nr == 5 || nr == 6 || nr == 7) { return tileImages[0]; }


        if(nr == 8) { return tileImages[10]; }

        else { return tileImages[9];  }


    }
}
