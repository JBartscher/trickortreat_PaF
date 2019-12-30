package main.java;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import main.java.map.Tile;


// Constructor of MapRenderer calls initGraphics
public class GraphicsUtility {


    // tileset 12x12 => 144 tiles
    public static Image[] tileImages = new Image[72];
    public  static Image imageCandy;

    public static void initGraphics(){
        initTileImages();
        initCandyImage();
    }

    public static void initTileImages()
    {
        Image image = new Image(GraphicsUtility.class.getResourceAsStream("tileset.png"));

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 7; x++) {
                tileImages[y * 7 + x] = new WritableImage(image.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
        System.out.println("fertig");
    }

    public static Image getTileImage(int nr) {

        //grass tiles = 54,55,61,62
        if (nr == 1) {
            return tileImages[55];
        }
        if (nr == 2) {
            return tileImages[55];
        }
        if (nr == 3) {
            return tileImages[62];
        }



        // SmallHouse - poor
        if (nr == 31) {
            return tileImages[28];
        } //top-left
        if (nr == 32) {
            return tileImages[29];
        } //top-right
        if (nr == 33) {
            return tileImages[35];
        } //down-left
        if (nr == 34) {
            return tileImages[36];
        } //down-right
        if (nr == 35) {
            return tileImages[21];
        } //down-left lights-out

        // BigHouse - poor
        if (nr == 41) {
            return tileImages[30];
        } //top-left
        if (nr == 42) {
            return tileImages[31];
        } //top-middle
        if (nr == 43) {
            return tileImages[32];
        } //top-right
        if (nr == 44) {
            return tileImages[37];
        } //down-left
        if (nr == 45) {
            return tileImages[38];
        } //down-middle
        if (nr == 46) {
            return tileImages[39];
        } //top-right
        if (nr == 47) {
            return tileImages[23];
        } //down-left lights-out
        if (nr == 48) {
            return tileImages[25];
        } //down-right lights-out


        // SmallHouse - normal
        if (nr == 51) {
            return tileImages[49];
        } //top-left
        if (nr == 52) {
            return tileImages[50];
        } //top-right
        if (nr == 53) {
            return tileImages[56];
        } //down-left
        if (nr == 54) {
            return tileImages[57];
        } //down-right
        if (nr == 55) {
            return tileImages[42];
        } //down-left lights-out

        // BigHouse - normal
        if (nr == 61) {
            return tileImages[51];
        } //top-left
        if (nr == 62) {
            return tileImages[52];
        } //top-middle
        if (nr == 63) {
            return tileImages[53];
        } //top-right
        if (nr == 64) {
            return tileImages[58];
        } //down-left
        if (nr == 65) {
            return tileImages[59];
        } //down-middle
        if (nr == 66) {
            return tileImages[60];
        } //top-right
        if (nr == 67) {
            return tileImages[44];
        } //down-left lights-out
        if (nr == 68) {
            return tileImages[46];
        } //down-right lights-out


        // SmallHouse - rich
        if (nr == 71) {
            return tileImages[7];
        } //top-left
        if (nr == 72) {
            return tileImages[8];
        } //top-right
        if (nr == 73) {
            return tileImages[14];
        } //down-left
        if (nr == 74) {
            return tileImages[15];
        } //down-right
        if (nr == 75) {
            return tileImages[0];
        } //down-left lights-out

        // BigHouse - rich
        if (nr == 81) {
            return tileImages[9];
        } //top-left
        if (nr == 82) {
            return tileImages[10];
        } //top-middle
        if (nr == 83) {
            return tileImages[11];
        } //top-right
        if (nr == 84) {
            return tileImages[16];
        } //down-left
        if (nr == 85) {
            return tileImages[17];
        } //down-middle
        if (nr == 86) {
            return tileImages[18];
        } //top-right
        if (nr == 87) {
            return tileImages[2];
        } //down-left lights-out
        if (nr == 88) {
            return tileImages[4];
        } //down-right lights-out

        // Townhall
        // first row
        if (nr == 90) {
            return tileImages[47];
        }
        if (nr == 91) {
            return tileImages[1];
        }
        if (nr == 92) {
            return tileImages[1];
        }
        if (nr == 93) {
            return tileImages[1];
        }
        if (nr == 94) {
            return tileImages[1];
        }
        if (nr == 95) {
            return tileImages[48];
        }
        //second row
        if (nr == 96) {
            return tileImages[3];
        }
        if (nr == 97) {
            return tileImages[22];
        }
        if (nr == 98) {
            return tileImages[24];
        }
        if (nr == 99) {
            return tileImages[43];
        }
        if (nr == 100) {
            return tileImages[61];
        }
        if (nr == 101) {
            return tileImages[45];
        }
        //third row
        if (nr == 102) {
            return tileImages[5];
        }
        if (nr == 103) {
            return tileImages[6];
        }
        if (nr == 104) {
            return tileImages[19];
        }
        if (nr == 105) {
            return tileImages[20];
        }
        if (nr == 106) {
            return tileImages[33];
        }
        if (nr == 107) {
            return tileImages[34];
        }
        //fourth row
        if (nr == 108) {
            return tileImages[12];
        }
        if (nr == 109) {
            return tileImages[13];
        }
        if (nr == 110) {
            return tileImages[26];
        }
        if (nr == 111) {
            return tileImages[27];
        }
        if (nr == 112) {
            return tileImages[40];
        }
        if (nr == 113) {
            return tileImages[41];
        }


        if (nr == 0) {
            return tileImages[1];
        } else {
            return tileImages[54];
        }


    }

    public static void initCandyImage() {
        imageCandy = new Image(GraphicsUtility.class.getResourceAsStream("candy.png"));
    }

    public static Image getCandyImage() {
        return imageCandy;
    }
}
