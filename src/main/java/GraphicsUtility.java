package main.java;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import main.java.map.Tile;


// Constructor of MapRenderer calls initGraphics
public class GraphicsUtility {


    // tileset 12x12 => 144 tiles
    public static Image[] tileImages = new Image[72];
    public static Image[] tileMansionImages = new Image[12];
    public static Image[] tileTreesImages = new Image[40];


    public  static Image candyImage;

    // TODO: ÜBERGANGSWEISE, wird später zum TileSet hinzugefügt
    public static Image streetTileGras;
    public static Image streetTileSand;
    public static Image streetTileDesert;
    public static Image streetTileSnow;

    public static Image grasGroundTile;
    public static Image grasDeko1Tile;
    public static Image grasDeko2Tile;
    public static Image grasDeko3Tile;

    public static Image sandGroundTile;
    public static Image sandDeko1Tile;
    public static Image sandDeko2Tile;
    public static Image sandDeko3Tile;


    public static Image desertGroundTile;


    public static Image snowGroundTile;
    public static Image snowDeko1Tile;
    public static Image snowDeko2Tile;
    public static Image snowDeko3Tile;

    public static Image keyImage;


    public static Image centreTile;
    public static Image borderTile;

    public static Image witchDoor;




    public static void initGraphics(){
        initTileImages();
        initImages();


    }

    public static void initTileImages()
    {
        Image image = new Image(GraphicsUtility.class.getResourceAsStream("tileset.png"));

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 7; x++) {
                tileImages[y * 7 + x] = new WritableImage(image.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }

        Image imageMansion = new Image(GraphicsUtility.class.getResourceAsStream("alice_cooper_villa_complete.png"));
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 4; x++) {
                tileMansionImages[y * 4 + x] = new WritableImage(imageMansion.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }

        Image imageTrees = new Image(GraphicsUtility.class.getResourceAsStream("trees.png"));
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 8; x++) {
                tileTreesImages[y * 8 + x] = new WritableImage(imageTrees.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }


        System.out.println("fertig");





        // TODO: ÜBERGANGSWEISE, wird später überarbeitet



        Image imageTileSetGras = new Image(GraphicsUtility.class.getResourceAsStream("tileset_gras.png"));
        Image imageTileSet = new Image(GraphicsUtility.class.getResourceAsStream("tileset_ground.png"));


        centreTile = new WritableImage(imageTileSet.getPixelReader(), 3 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );


        //grasGroundTile = new WritableImage(imageTileSet.getPixelReader(), 0 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE );
        grasGroundTile = new WritableImage(imageTileSetGras.getPixelReader(), 0 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE );

        grasDeko1Tile = new WritableImage(imageTileSetGras.getPixelReader(), 0 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        grasDeko2Tile = new WritableImage(imageTileSetGras.getPixelReader(), 1 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        grasDeko3Tile = new WritableImage(imageTileSetGras.getPixelReader(), 0 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );


        //sandGroundTile = new WritableImage(imageTileSet.getPixelReader(), 0 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE );
        sandGroundTile = new WritableImage(imageTileSetGras.getPixelReader(), 3 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        sandDeko1Tile = new WritableImage(imageTileSetGras.getPixelReader(), 4 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        sandDeko2Tile = new WritableImage(imageTileSetGras.getPixelReader(), 5 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        sandDeko3Tile = new WritableImage(imageTileSetGras.getPixelReader(), 3 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );


        desertGroundTile = new WritableImage(imageTileSet.getPixelReader(), 0 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE );
        snowGroundTile = new WritableImage(imageTileSet.getPixelReader(), 0 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE );

        streetTileGras = new WritableImage(imageTileSetGras.getPixelReader(), 2 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        streetTileSand = new WritableImage(imageTileSetGras.getPixelReader(), 5 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        streetTileDesert = new WritableImage(imageTileSet.getPixelReader(), 2 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        streetTileSnow = new WritableImage(imageTileSet.getPixelReader(), 2 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );

        snowDeko1Tile = new WritableImage(imageTileSet.getPixelReader(), 7 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        snowDeko2Tile = new WritableImage(imageTileSet.getPixelReader(), 7 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );
        snowDeko3Tile = new WritableImage(imageTileSet.getPixelReader(), 4 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );


        borderTile = new WritableImage(imageTileSet.getPixelReader(), 3 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE );

    }

    public static void setTextProperties(Text text, String style, Color color, double x, double y) {
        text.setStyle(style);
        text.setFill(color);
        text.setX(x);
        text.setY(y);

    }

    public static void setImageProperties(ImageView img, double x, double y) {
        img.setX(x);
        img.setY(y);
    }


    public static Image getTileImage(int nr) {

        if(nr == 1) {
            return grasGroundTile;
        } else if(nr == 2) {
            return grasDeko1Tile;
        } else if(nr == 3) {
            return grasDeko2Tile;
        } else if (nr == 4) {
            return grasDeko3Tile;
        } else if (nr == 5) {
            return sandGroundTile;
        } else if (nr == 6) {
            return desertGroundTile;
        } else if (nr == 7) {
            return snowGroundTile;
        } else if (nr == 8) {
            return snowDeko1Tile;
        } else if (nr == 9) {
            return snowDeko2Tile;
        }else if (nr == 10) {
            return snowDeko3Tile;
        } else if(nr == 11) {
            return sandDeko1Tile;
        } else if(nr == 12) {
            return sandDeko2Tile;
        } else if(nr == 13) {
            return sandDeko3Tile;
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

        // Villa
        if (nr == 212) {
            return tileMansionImages[0];
        }
        if (nr == 213) {
            return tileMansionImages[1];
        }
        if (nr == 214) {
            return tileMansionImages[2];
        }
        if (nr == 215) {
            return tileMansionImages[3];
        }

        if (nr == 216) {
            return tileMansionImages[4];
        }
        if (nr == 217) {
            return tileMansionImages[5];
        }
        if (nr == 218) {
            return tileMansionImages[6];
        }
        if (nr == 219) {
            return tileMansionImages[7];
        }

        if (nr == 220) {
            return tileMansionImages[8];
        }
        if (nr == 221) {
            return tileMansionImages[9];
        }
        if (nr == 222) {
            return tileMansionImages[10];
        }
        if (nr == 223) {
            return tileMansionImages[11];
        }

        if ( nr == 300) {

            return witchDoor;
        }



        if ( nr == -1) {
            return tileTreesImages[0];
        }

        if ( nr == -2) {
            return tileTreesImages[1];
        }

        if ( nr == -3) {
            return tileTreesImages[2];
        }

        if ( nr == -4) {
            return tileTreesImages[3];
        }

        if ( nr == -5) {
            return tileTreesImages[4];
        }

        if ( nr == -6) {
            return tileTreesImages[5];
        }

        if ( nr == -7) {
            return tileTreesImages[6];
        }

        if ( nr == -8) {
            return tileTreesImages[7];
        }


        if (nr == 20 ) {
            return borderTile;
        }
        // TODO: VORERST STREET-TILE
        if (nr == 21) {
            return streetTileGras;
        } else if (nr == 22) {
            return streetTileSand;
        } else if (nr == 23) {
            return streetTileDesert;
        } else if(nr == 24) {
            return streetTileSnow;
        } else if(nr == 25) {
            return centreTile;
        }  else {
            return tileImages[54];
        }
    }

    public static void initImages() {
        candyImage = new Image(GraphicsUtility.class.getResourceAsStream("candy.png"));
        keyImage = new Image(GraphicsUtility.class.getResourceAsStream("key.png"));
        witchDoor = new Image(GraphicsUtility.class.getResourceAsStream("witch_door.png"));
    }

    public static Image getCandyImage() {
        return candyImage;
    }

    public static Image getKeyImage() {
        return keyImage;
    }


}
