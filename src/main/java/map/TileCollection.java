package main.java.map;//TODO: Ich check Enums nicht :( Wenn jemand das durch einen Enum ersetzten könnte wäre das Töfte.

import main.java.gameobjects.mapobjects.Mansion;
import main.java.gameobjects.mapobjects.TownHall;

/**
 * The TileCollection holds all possible Tiles as a static reference.
 */
public class TileCollection {


    private final static int GRASS = 1;
    public final static Tile GRASS_TILE = new Tile(GRASS, false);
    private final static int STREET = 2;
    public final static Tile STREET_TILE = new Tile(STREET, true);


    //Poor Houses
    public static Tile[][] getPoorSmallHouseUnvisitedTiles() {
        Tile[][] smallHouse = new Tile[2][2];
        smallHouse[0][0] = new Tile(31);
        smallHouse[0][1] = new Tile(32);

        smallHouse[1][0] = new Tile(33);
        smallHouse[1][1] = new Tile(34, true);
        return smallHouse;
    }

    public final static Tile[][] getPoorSmallHouseVisitedTiles() {
        Tile[][] smallHouse = getPoorSmallHouseUnvisitedTiles();
        smallHouse[1][0] = new Tile(35);
        return smallHouse;
    }

    public static Tile[][] getPoorBigHouseUnvisitedTiles() {
        Tile[][] bigHouse = new Tile[2][3];
        bigHouse[0][0] = new Tile(41);
        bigHouse[0][1] = new Tile(42);
        bigHouse[0][2] = new Tile(43);

        bigHouse[1][0] = new Tile(44);
        bigHouse[1][1] = new Tile(45, true);
        bigHouse[1][2] = new Tile(46);

        return bigHouse;
    }

    public final static Tile[][] getPoorBigHouseVisitedTiles() {
        Tile[][] bigHouse = getPoorBigHouseUnvisitedTiles();
        bigHouse[1][0] = new Tile(47);
        bigHouse[1][2] = new Tile(48);
        return bigHouse;
    }

    //Normal Houses
    public static Tile[][] getNormalSmallHouseUnvisitedTiles() {
        Tile[][] smallHouse = new Tile[2][2];
        smallHouse[0][0] = new Tile(51);
        smallHouse[0][1] = new Tile(52);

        smallHouse[1][0] = new Tile(53);
        smallHouse[1][1] = new Tile(54, true);
        return smallHouse;
    }

    public final static Tile[][] getNormalSmallHouseVisitedTiles() {
        Tile[][] smallHouse = getNormalSmallHouseUnvisitedTiles();
        smallHouse[1][0] = new Tile(55);
        return smallHouse;
    }

    public static Tile[][] getNormalBigHouseUnvisitedTiles() {
        Tile[][] bigHouse = new Tile[2][3];
        bigHouse[0][0] = new Tile(61);
        bigHouse[0][1] = new Tile(62);
        bigHouse[0][2] = new Tile(63);

        bigHouse[1][0] = new Tile(64);
        bigHouse[1][1] = new Tile(65, true);
        bigHouse[1][2] = new Tile(66);

        return bigHouse;
    }

    public final static Tile[][] getNormalBigHouseVisitedTiles() {
        Tile[][] bigHouse = getNormalBigHouseUnvisitedTiles();
        bigHouse[1][0] = new Tile(67);
        bigHouse[1][2] = new Tile(68);
        return bigHouse;
    }

    //Rich Houses
    public static Tile[][] getRichSmallHouseUnvisitedTiles() {
        Tile[][] smallHouse = new Tile[2][2];
        smallHouse[0][0] = new Tile(71);
        smallHouse[0][1] = new Tile(72);

        smallHouse[1][0] = new Tile(73);
        smallHouse[1][1] = new Tile(74, true);
        return smallHouse;
    }

    public final static Tile[][] getRichSmallHouseVisitedTiles() {
        Tile[][] smallHouse = getRichSmallHouseUnvisitedTiles();
        smallHouse[1][0] = new Tile(75);
        return smallHouse;
    }

    public static Tile[][] getRichBigHouseUnvisitedTiles() {
        Tile[][] bigHouse = new Tile[2][3];
        bigHouse[0][0] = new Tile(81);
        bigHouse[0][1] = new Tile(82);
        bigHouse[0][2] = new Tile(83);

        bigHouse[1][0] = new Tile(84);
        bigHouse[1][1] = new Tile(85, true);
        bigHouse[1][2] = new Tile(86);

        return bigHouse;
    }

    public final static Tile[][] getRichBigHouseVisitedTiles() {
        Tile[][] bigHouse = getRichBigHouseUnvisitedTiles();
        bigHouse[1][0] = new Tile(87);
        bigHouse[1][2] = new Tile(88);
        return bigHouse;
    }

    // TownHall
    public final static Tile[][] getTownHallTiles() {
        Tile[][] townHall = new Tile[TownHall.TOWN_HALL_HEIGHT][TownHall.TOWN_HALL_WIDTH];
        townHall[0][0] = new Tile(90);
        townHall[0][1] = new Tile(91);
        townHall[0][2] = new Tile(92);
        townHall[0][3] = new Tile(93);
        townHall[0][4] = new Tile(94);
        townHall[0][5] = new Tile(95);

        townHall[1][0] = new Tile(96);
        townHall[1][1] = new Tile(97);
        townHall[1][2] = new Tile(98);
        townHall[1][3] = new Tile(99);
        townHall[1][4] = new Tile(100);
        townHall[1][5] = new Tile(101);

        townHall[2][0] = new Tile(102);
        townHall[2][1] = new Tile(103);
        townHall[2][2] = new Tile(104);
        townHall[2][3] = new Tile(105);
        townHall[2][4] = new Tile(106);
        townHall[2][5] = new Tile(107);

        townHall[3][0] = new Tile(108);
        townHall[3][1] = new Tile(109);
        townHall[3][2] = new Tile(110, true);
        townHall[3][3] = new Tile(111, true);
        townHall[3][4] = new Tile(112);
        townHall[3][5] = new Tile(113);

        return townHall;
    }

    // Mansion
    public final static Tile[][] getMansonTiles() {
        Tile[][] mansion = new Tile[Mansion.MANSION_HEIGHT][Mansion.MANSION_WIDTH];

        mansion[0][0] = new Tile(212);
        mansion[0][1] = new Tile(213);
        mansion[0][2] = new Tile(214);
        mansion[0][3] = new Tile(215);

        mansion[1][0] = new Tile(216);
        mansion[1][1] = new Tile(217);
        mansion[1][2] = new Tile(218);
        mansion[1][3] = new Tile(219);

        mansion[2][0] = new Tile(220);
        mansion[2][1] = new Tile(221, true);
        mansion[2][2] = new Tile(222, true);
        mansion[2][3] = new Tile(223);





        //mansion[4][3] = new Tile(34, true);

        return mansion;
    }


}