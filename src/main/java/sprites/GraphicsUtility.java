package main.java.sprites;

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
    public static Image[] tileTreesImages = new Image[40];
    public static Image[][] tileMansionImages = new Image[5][8];
    public static Image[][] tileTownHallImages = new Image[5][9];
    public static Image[] tileSetCentre = new Image[24];

    public static Image[] tileWitchHouse = new Image[3];

    public static Image pausedImage;
    public static Image playImage;
    public static Image muteImage;
    public static Image unmutedImage;



    public static Image candyImage;

    // TODO: ÜBERGANGSWEISE, wird später zum TileSet hinzugefügt
    public static Image streetTileGras;
    public static Image streetTileSand;
    public static Image streetTileDesert;
    public static Image streetTileSnow;
    public static Image streetTileCentre;

    public static Image grasGroundTile;
    public static Image grasDeko1Tile;
    public static Image grasDeko2Tile;
    public static Image grasDeko3Tile;

    public static Image earthGroundTile;
    public static Image earthDeko1Tile;
    public static Image earthDeko2Tile;
    public static Image earthDeko3Tile;


    public static Image desertGroundTile;
    public static Image desertDeko1Tile;
    public static Image desertDeko2Tile;
    public static Image desertDeko3Tile;
    public static Image desertDeko4Tile;


    public static Image snowGroundTile;
    public static Image snowDeko1Tile;
    public static Image snowDeko2Tile;
    public static Image snowDeko3Tile;
    public static Image snowDeko4Tile;
    public static Image snowDeko5Tile;

    public static Image keyImage;


    public static Image centreTile;
    public static Image centreLightGroundTile;
    public static Image centreDarkGroundTile;
    public static Image centreGrasGroundTile;
    public static Image borderTile;
    public static Image witchDoor;


    public static void initGraphics() {
        initTileImages();
        initImages();
    }

    public static void initTileImages() {
        Image image = new Image(GraphicsUtility.class.getResourceAsStream("tileset.png"));

        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 7; x++) {
                tileImages[y * 7 + x] = new WritableImage(image.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }

        Image imageTownhall = new Image(GraphicsUtility.class.getResourceAsStream("townhall.png"));
        for (int y = 0; y < 9; y++) {
            for (int x = 0; x < 5; x++) {
                tileTownHallImages[x][y] = new WritableImage(imageTownhall.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }

        Image imageTrees = new Image(GraphicsUtility.class.getResourceAsStream("trees.png"));
        for (int y = 0; y < 1; y++) {
            for (int x = 0; x < 8; x++) {
                tileTreesImages[y * 8 + x] = new WritableImage(imageTrees.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }

        Image imageMansion = new Image(GraphicsUtility.class.getResourceAsStream("mansion.png"));
        for (int y = 0; y < 8; y++) {
            for (int x = 0; x < 5; x++) {
                tileMansionImages[x][y] = new WritableImage(imageMansion.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }

        Image imageWitchHouse = new Image(GraphicsUtility.class.getResourceAsStream("witch_house.png"));
        for (int x = 0; x < 3; x++) {
            tileWitchHouse[x] = new WritableImage(imageWitchHouse.getPixelReader(), x * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
        }

        Image imageCentre = new Image(GraphicsUtility.class.getResourceAsStream("tileset_centre.png"));
        for (int y = 0; y < 4; y++) {
            for (int x = 0; x < 6; x++) {
                tileSetCentre[y * 6 + x] = new WritableImage(imageCentre.getPixelReader(), x * (Tile.TILE_SIZE), y * (Tile.TILE_SIZE), Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }



        System.out.println("fertig");

        Image imageTileSetGras = new Image(GraphicsUtility.class.getResourceAsStream("tileset_gras.png"));
        Image imageTileSet = new Image(GraphicsUtility.class.getResourceAsStream("tileset_ground.png"));
        Image imageTileSnowAndDesert = new Image(GraphicsUtility.class.getResourceAsStream("tileset_snow_desert.png"));


        //centreTile = new WritableImage(imageTileSet.getPixelReader(), 3 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        centreTile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 0 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        //centreGroundTile = new WritableImage(imageTileSet.getPixelReader(), 5 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        centreLightGroundTile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 1 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        centreDarkGroundTile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 2 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        centreGrasGroundTile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 4 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);


        //grasGroundTile = new WritableImage(imageTileSet.getPixelReader(), 0 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE );
        grasGroundTile = new WritableImage(imageTileSetGras.getPixelReader(), 0 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE);

        grasDeko1Tile = new WritableImage(imageTileSetGras.getPixelReader(), 0 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        grasDeko2Tile = new WritableImage(imageTileSetGras.getPixelReader(), 1 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        grasDeko3Tile = new WritableImage(imageTileSetGras.getPixelReader(), 0 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);


        //sandGroundTile = new WritableImage(imageTileSet.getPixelReader(), 0 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE );
        earthGroundTile = new WritableImage(imageTileSetGras.getPixelReader(), 3 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        earthDeko1Tile = new WritableImage(imageTileSetGras.getPixelReader(), 4 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        earthDeko2Tile = new WritableImage(imageTileSetGras.getPixelReader(), 5 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        earthDeko3Tile = new WritableImage(imageTileSetGras.getPixelReader(), 3 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);


        desertGroundTile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 0 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE);
        desertDeko1Tile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 3 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        desertDeko2Tile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 4 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        desertDeko3Tile = new WritableImage(imageTileSet.getPixelReader(), 7 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        desertDeko4Tile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 5 * (Tile.TILE_SIZE), 1 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);


        //snowGroundTile = new WritableImage(imageTileSet.getPixelReader(), 0 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32), Tile.TILE_SIZE, Tile.TILE_SIZE);
        snowGroundTile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 0 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);

        streetTileGras = new WritableImage(imageTileSetGras.getPixelReader(), 2 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        streetTileSand = new WritableImage(imageTileSetGras.getPixelReader(), 5 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        streetTileDesert = new WritableImage(imageTileSet.getPixelReader(), 2 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        streetTileSnow = new WritableImage(imageTileSet.getPixelReader(), 2 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        streetTileCentre = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 3 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);



        snowDeko1Tile = new WritableImage(imageTileSet.getPixelReader(), 7 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        snowDeko2Tile = new WritableImage(imageTileSet.getPixelReader(), 7 * (Tile.TILE_SIZE), 2 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        snowDeko3Tile = new WritableImage(imageTileSet.getPixelReader(), 4 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        snowDeko4Tile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 4 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);
        snowDeko5Tile = new WritableImage(imageTileSnowAndDesert.getPixelReader(), 5 * (Tile.TILE_SIZE), 3 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);


        borderTile = new WritableImage(imageTileSet.getPixelReader(), 3 * (Tile.TILE_SIZE), 0 * (Tile.TILE_SIZE + 32) + 32, Tile.TILE_SIZE, Tile.TILE_SIZE);

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

        if (nr == 1) {
            return grasGroundTile;
        } else if (nr == 2) {
            return grasDeko1Tile;
        } else if (nr == 3) {
            return grasDeko2Tile;
        } else if (nr == 4) {
            return grasDeko3Tile;
        } else if (nr == 5) {
            return earthGroundTile;
        } else if (nr == 6) {
            return earthDeko1Tile;
        } else if (nr == 7) {
            return earthDeko2Tile;
        } else if (nr == 8) {
            return earthDeko3Tile;
        } else if (nr == 9) {
            return desertGroundTile;
        } else if (nr == 10) {
            return desertDeko1Tile;
        } else if (nr == 11) {
            return desertDeko2Tile;
        } else if (nr == -12) {
            return desertDeko3Tile;
        } else if (nr == -13) {
            return desertDeko4Tile;
        } else if (nr == 14) {
            return snowGroundTile;
        } else if (nr == 15) {
            return snowDeko1Tile;
        } else if (nr == 16) {
            return snowDeko2Tile;
        } else if (nr == 17) {
            return snowDeko3Tile;
        } else if (nr == 18) {
            return snowDeko4Tile;
        } else if (nr == -19) {
            return snowDeko5Tile;
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

        if (nr == 69) {
            return tileWitchHouse[1];
        }

        /**
         * Door
         */
        if (nr == 70) {
            return tileWitchHouse[0];
        }

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

        // TownHall - outside
        // first row
        if (nr == 90) {
            return tileTownHallImages[0][0];
        }
        if (nr == 91) {
            return tileTownHallImages[1][0];
        }
        if (nr == 92) {
            return tileTownHallImages[2][0];
        }
        if (nr == 93) {
            return tileTownHallImages[3][0];
        }
        if (nr == 94) {
            return tileTownHallImages[4][0];
        }

        //second row
        if (nr == 96) {
            return tileTownHallImages[0][1];
        }
        if (nr == 97) {
            return tileTownHallImages[1][1];
        }
        if (nr == 98) {
            return tileTownHallImages[2][1];
        }
        if (nr == 99) {
            return tileTownHallImages[3][1];
        }
        if (nr == 100) {
            return tileTownHallImages[4][1];
        }

        //third row
        if (nr == 102) {
            return tileTownHallImages[0][2];
        }
        if (nr == 103) {
            return tileTownHallImages[1][2];
        }
        if (nr == 104) {
            return tileTownHallImages[2][2];
        }
        if (nr == 105) {
            return tileTownHallImages[3][2];
        }
        if (nr == 106) {
            return tileTownHallImages[4][2];
        }

        //fourth row
        if (nr == 108) {
            return tileTownHallImages[0][3];
        }
        if (nr == 109) {
            return tileTownHallImages[1][3];
        }
        if (nr == 110) {
            return tileTownHallImages[2][3];
        }
        if (nr == 111) {
            return tileTownHallImages[3][3];
        }
        if (nr == 112) {
            return tileTownHallImages[4][3];
        }

        // TownHall - outside
        // first row
        if (nr == 113) {
            return tileTownHallImages[0][4];
        }
        if (nr == 114) {
            return tileTownHallImages[1][4];
        }
        if (nr == 115) {
            return tileTownHallImages[2][4];
        }
        if (nr == 116) {
            return tileTownHallImages[3][4];
        }
        if (nr == 117) {
            return tileTownHallImages[4][4];
        }

        //second row
        if (nr == 118) {
            return tileTownHallImages[0][5];
        }
        if (nr == 119) {
            return tileTownHallImages[1][5];
        }
        if (nr == 120) {
            return tileTownHallImages[2][5];
        }
        if (nr == 121) {
            return tileTownHallImages[3][5];
        }
        if (nr == 122) {
            return tileTownHallImages[4][5];
        }

        //third row
        if (nr == 123) {
            return tileTownHallImages[0][6];
        }
        if (nr == 124) {
            return tileTownHallImages[1][6];
        }
        if (nr == 125) {
            return tileTownHallImages[2][6];
        }
        if (nr == 126) {
            return tileTownHallImages[3][6];
        }
        if (nr == 127) {
            return tileTownHallImages[4][6];
        }

        //fourth row
        if (nr == 128) {
            return tileTownHallImages[0][7];
        }
        if (nr == 129) {
            return tileTownHallImages[1][7];
        }
        if (nr == 130) {
            return tileTownHallImages[2][7];
        }
        if (nr == 131) {
            return tileTownHallImages[3][7];
        }
        if (nr == 132) {
            return tileTownHallImages[4][7];
        }

        // townhall EmptyKey Field/Table
        if (nr == 133) {
            return tileTownHallImages[0][8];
        }


        // Villa - outside
        // first row
        if (nr == 212) {
            return tileMansionImages[0][0];
        }
        if (nr == 213) {
            return tileMansionImages[1][0];
        }
        if (nr == 214) {
            return tileMansionImages[2][0];
        }
        if (nr == 215) {
            return tileMansionImages[3][0];
        }
        if (nr == 216) {
            return tileMansionImages[4][0];
        }
        // second row

        if (nr == 217) {
            return tileMansionImages[0][1];
        }
        if (nr == 218) {
            return tileMansionImages[1][1];
        }
        if (nr == 219) {
            return tileMansionImages[2][1];
        }
        if (nr == 220) {
            return tileMansionImages[3][1];
        }
        if (nr == 221) {
            return tileMansionImages[4][1];
        }
        // third row

        if (nr == 222) {
            return tileMansionImages[0][2];
        }
        if (nr == 223) {
            return tileMansionImages[1][2];
        }
        if (nr == 224) {
            return tileMansionImages[2][2];
        }
        if (nr == 225) {
            return tileMansionImages[3][2];
        }
        if (nr == 226) {
            return tileMansionImages[4][2];
        }
        // fourth row
        if (nr == 227) {
            return tileMansionImages[0][3];
        }
        if (nr == 228) {
            return tileMansionImages[1][3];
        }
        if (nr == 229) {
            return tileMansionImages[2][3];
        }
        if (nr == 230) {
            return tileMansionImages[3][3];
        }
        if (nr == 231) {
            return tileMansionImages[4][3];
        }

        // Villa - inside
        // first row

        // first row
        if (nr == 232) {
            return tileMansionImages[0][4];
        }
        if (nr == 233) {
            return tileMansionImages[1][4];
        }
        if (nr == 234) {
            return tileMansionImages[2][4];
        }
        if (nr == 235) {
            return tileMansionImages[3][4];
        }
        if (nr == 236) {
            return tileMansionImages[4][4];
        }
        // second row
        if (nr == 237) {
            return tileMansionImages[0][5];
        }
        if (nr == 238) {
            return tileMansionImages[1][5];
        }
        if (nr == 239) {
            return tileMansionImages[2][5];
        }
        if (nr == 240) {
            return tileMansionImages[3][5];
        }
        if (nr == 241) {
            return tileMansionImages[4][5];
        }
        // third row
        if (nr == 242) {
            return tileMansionImages[0][6];
        }
        if (nr == 243) {
            return tileMansionImages[1][6];
        }
        if (nr == 244) {
            return tileMansionImages[2][6];
        }
        if (nr == 245) {
            return tileMansionImages[3][6];
        }
        if (nr == 246) {
            return tileMansionImages[4][6];
        }
        // fourth row
        if (nr == 247) {
            return tileMansionImages[0][7];
        }
        if (nr == 248) {
            return tileMansionImages[1][7];
        }
        if (nr == 249) {
            return tileMansionImages[2][7];
        }
        if (nr == 250) {
            return tileMansionImages[3][7];
        }
        if (nr == 251) {
            return tileMansionImages[4][7];
        }

        if(nr == -300) {
            return tileSetCentre[0];
        }

        if(nr == -301) {
            return tileSetCentre[1];
        }

        if(nr == -306) {
            return tileSetCentre[6];
        }

        if(nr == -307) {
            return tileSetCentre[7];
        }

        if(nr == -308) {
            return tileSetCentre[8];
        }

        if(nr == -309) {
            return tileSetCentre[9];
        }

        if(nr == -310) {
            return tileSetCentre[10];
        }

        if(nr == -311) {
            return tileSetCentre[11];
        }

        if(nr == -312) {
            return tileSetCentre[12];
        }

        if(nr == -320) {
            return tileSetCentre[20];
        }

        if(nr == -322) {
            return tileSetCentre[22];
        }

        if(nr == 325){
            return centreTile;
        }

        if(nr == 326){
            return centreLightGroundTile;
        }

        if(nr == 327){
            return centreDarkGroundTile;
        }




        if (nr == -1) {
            return tileTreesImages[0];
        }

        if (nr == -2) {
            return tileTreesImages[1];
        }

        if (nr == -3) {
            return tileTreesImages[2];
        }

        if (nr == -4) {
            return tileTreesImages[3];
        }

        if (nr == -5) {
            return tileTreesImages[4];
        }

        if (nr == -6) {
            return tileTreesImages[5];
        }

        if (nr == -7) {
            return tileTreesImages[6];
        }

        if (nr == -8) {
            return tileTreesImages[7];
        }


        if (nr == 20) {
            return borderTile;
        }
        // TODO: VORERST STREET-TILE
        if (nr == 21) {
            return streetTileGras;
        } else if (nr == 22) {
            return streetTileSand;
        } else if (nr == 23) {
            return streetTileDesert;
        } else if (nr == 24) {
            return streetTileSnow;
        } else if (nr == 25) {
            return streetTileCentre;
        } else if(nr == 26){
            return centreGrasGroundTile;
            }
        else {
            return tileImages[54];
        }
    }

    public static void initImages() {
        //candyImage = new Image(GraphicsUtility.class.getResourceAsStream("candy.png"));

        candyImage = new Image(GraphicsUtility.class.getResourceAsStream("candy.png"));

        keyImage = new Image(GraphicsUtility.class.getResourceAsStream("key.png"));
        witchDoor = new Image(GraphicsUtility.class.getResourceAsStream("witch_door.png"));

        // TODO : FUNKTIONIERT NICHT....
        //pausedImage = new Image(GraphicsUtility.class.getResourceAsStream("paused.png"));

    }

    public static Image getPausedImage() {
        return pausedImage;
    }

    public static Image getCandyImage() {
        return candyImage;
    }

    public static Image getKeyImage() {
        return keyImage;
    }


}
