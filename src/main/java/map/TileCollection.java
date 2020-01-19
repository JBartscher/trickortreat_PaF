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

    // POOR - HOUSES

    /**
     * get the tiles for a unvisited POOR SMALL House
     *
     * @return tiles for a POOR SMALL House
     */
    public static Tile[][] getPoorSmallHouseUnvisitedTiles() {
        Tile[][] smallHouse = new Tile[2][2];
        smallHouse[0][0] = new Tile(31);
        smallHouse[0][1] = new Tile(32);

        smallHouse[1][0] = new Tile(33);
        smallHouse[1][1] = new Tile(34, true);
        return smallHouse;
    }

    /**
     * get the visited tiles for a POOR SMALL House
     *
     * @return tiles for a visited POOR SMALL House
     */
    public final static Tile[][] getPoorSmallHouseVisitedTiles() {
        Tile[][] smallHouse = getPoorSmallHouseUnvisitedTiles();
        smallHouse[1][0] = new Tile(35);
        return smallHouse;
    }

    /**
     * get the tiles for a unvisited POOR BIG House
     *
     * @return tiles for a POOR BIG House
     */
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

    /**
     * get the visited tiles for a POOR BIG House
     *
     * @return tiles for a visited POOR BIG House
     */
    public final static Tile[][] getPoorBigHouseVisitedTiles() {
        Tile[][] bigHouse = getPoorBigHouseUnvisitedTiles();
        bigHouse[1][0] = new Tile(47);
        bigHouse[1][2] = new Tile(48);
        return bigHouse;
    }

    // NORMAL - HOUSES

    /**
     * get the tiles for a unvisited NORMAL SMALL House
     *
     * @return tiles for a NORMAL SMALL House
     */
    public static Tile[][] getNormalSmallHouseUnvisitedTiles() {
        Tile[][] smallHouse = new Tile[2][2];
        smallHouse[0][0] = new Tile(51);
        smallHouse[0][1] = new Tile(52);

        smallHouse[1][0] = new Tile(53);
        smallHouse[1][1] = new Tile(54, true);
        return smallHouse;
    }

    /**
     * get the visited tiles for a NORMAL SMALL House
     *
     * @return tiles for a visited NORMAL SMALL House
     */
    public final static Tile[][] getNormalSmallHouseVisitedTiles() {
        Tile[][] smallHouse = getNormalSmallHouseUnvisitedTiles();
        smallHouse[1][0] = new Tile(55);
        return smallHouse;
    }

    /**
     * get the tiles for a unvisited NORMAL BIG House
     *
     * @return tiles for a NORMAL BIG House
     */
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

    /**
     * get the visited tiles for a NORMAL BIG House
     *
     * @return tiles for a visited NORMAL BIG House
     */
    public final static Tile[][] getNormalBigHouseVisitedTiles() {
        Tile[][] bigHouse = getNormalBigHouseUnvisitedTiles();
        bigHouse[1][0] = new Tile(67);
        bigHouse[1][2] = new Tile(68);
        return bigHouse;
    }

    // RICH - HOUSES

    /**
     * get the tiles for a unvisited RICH SMALL House
     *
     * @return tiles for a RICH SMALL House
     */
    public static Tile[][] getRichSmallHouseUnvisitedTiles() {
        Tile[][] smallHouse = new Tile[2][2];
        smallHouse[0][0] = new Tile(71);
        smallHouse[0][1] = new Tile(72);

        smallHouse[1][0] = new Tile(73);
        smallHouse[1][1] = new Tile(74, true);
        return smallHouse;
    }

    /**
     * get the visited tiles for a RICH SMALL House
     *
     * @return tiles for a visited RICH SMALL House
     */
    public final static Tile[][] getRichSmallHouseVisitedTiles() {
        Tile[][] smallHouse = getRichSmallHouseUnvisitedTiles();
        smallHouse[1][0] = new Tile(75);
        return smallHouse;
    }

    /**
     * get the tiles for a unvisited RICH BIG House
     *
     * @return tiles for a RICH BIG House
     */
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

    /**
     * get the visited tiles for a RICH BIG House
     *
     * @return tiles for a visited RICH BIG House
     */
    public final static Tile[][] getRichBigHouseVisitedTiles() {
        Tile[][] bigHouse = getRichBigHouseUnvisitedTiles();
        bigHouse[1][0] = new Tile(87);
        bigHouse[1][2] = new Tile(88);
        return bigHouse;
    }

    // GINGERBREAD - HOUSE

    public static Tile[][] getGingerbreadHouseUnvisitedTiles() {
        Tile[][] gingerbreadHouseHouse = new Tile[2][3];
        gingerbreadHouseHouse[0][0] = new Tile(61);
        gingerbreadHouseHouse[0][1] = new Tile(62);
        gingerbreadHouseHouse[0][2] = new Tile(63);

        gingerbreadHouseHouse[1][0] = new Tile(64);
        gingerbreadHouseHouse[1][1] = new Tile(300, true);
        gingerbreadHouseHouse[1][2] = new Tile(66);
        return gingerbreadHouseHouse;
    }

    // TODO: Ändern
    public static Tile[][] getGingerbreadHouseVisitedTiles() {
        Tile[][] gingerbreadHouseHouse = new Tile[2][3];
        gingerbreadHouseHouse[0][0] = new Tile(61);
        gingerbreadHouseHouse[0][1] = new Tile(62);
        gingerbreadHouseHouse[0][2] = new Tile(63);

        gingerbreadHouseHouse[1][0] = new Tile(1000);
        gingerbreadHouseHouse[1][1] = new Tile(300, true);
        gingerbreadHouseHouse[1][2] = new Tile(66);
        return gingerbreadHouseHouse;
    }

    // TOWNHALL - HOUSE

    /**
     * get the tiles of the outside front of the TownHall
     *
     * @return outside townhall tiles
     */
    public final static Tile[][] getTownHallTiles() {
        Tile[][] townHall = new Tile[TownHall.TOWN_HALL_HEIGHT][TownHall.TOWN_HALL_WIDTH];
        townHall[0][0] = new Tile(90);
        townHall[0][1] = new Tile(91);
        townHall[0][2] = new Tile(92);
        townHall[0][3] = new Tile(93);
        townHall[0][4] = new Tile(94);
        // townHall[0][5] = new Tile(95);

        townHall[1][0] = new Tile(96);
        townHall[1][1] = new Tile(97);
        townHall[1][2] = new Tile(98);
        townHall[1][3] = new Tile(99);
        townHall[1][4] = new Tile(100);
        // townHall[1][5] = new Tile(101);

        townHall[2][0] = new Tile(102);
        townHall[2][1] = new Tile(103);
        townHall[2][2] = new Tile(104);
        townHall[2][3] = new Tile(105);
        townHall[2][4] = new Tile(106);
        // townHall[2][5] = new Tile(107);

        townHall[3][0] = new Tile(108);
        townHall[3][1] = new Tile(109);
        townHall[3][2] = new Tile(110, true);
        townHall[3][3] = new Tile(111);
        townHall[3][4] = new Tile(112);
        // townHall[3][5] = new Tile(113);

        return townHall;
    }

    /**
     * get the tiles of the inside front of the TownHall
     *
     * @return inside townhall tiles
     */
    public final static Tile[][] getTownHallInsideTiles() {
        Tile[][] townHallInside = new Tile[TownHall.TOWN_HALL_HEIGHT][TownHall.TOWN_HALL_WIDTH];
        townHallInside[0][0] = new Tile(113);
        townHallInside[0][1] = new Tile(114);
        townHallInside[0][2] = new Tile(115);
        townHallInside[0][3] = new Tile(116);
        townHallInside[0][4] = new Tile(117);

        townHallInside[1][0] = new Tile(118);
        townHallInside[1][1] = new Tile(119);
        // KEY
        townHallInside[1][2] = new Tile(120);
        townHallInside[1][3] = new Tile(121);
        townHallInside[1][4] = new Tile(122);

        townHallInside[2][0] = new Tile(123);
        townHallInside[2][1] = new Tile(124);
        townHallInside[2][2] = new Tile(125);
        townHallInside[2][3] = new Tile(126);
        townHallInside[2][4] = new Tile(127);

        townHallInside[3][0] = new Tile(128);
        townHallInside[3][1] = new Tile(129);
        townHallInside[3][2] = new Tile(130, true);
        townHallInside[3][3] = new Tile(131);
        townHallInside[3][4] = new Tile(132);

        return townHallInside;
    }

    /**
     * get the table tile without the key
     *
     * @return tile without key
     */
    public static Tile getMissingKeyTile() {
        return new Tile(133);
    }

    // MANISON - HOUSE

    // Mansion
    public final static Tile[][] getMansionOutsideTiles() {
        Tile[][] mansion = new Tile[Mansion.MANSION_HEIGHT][Mansion.MANSION_WIDTH];

        mansion[0][0] = new Tile(212);
        mansion[0][1] = new Tile(213);
        mansion[0][2] = new Tile(214);
        mansion[0][3] = new Tile(215);
        mansion[0][4] = new Tile(216);

        mansion[1][0] = new Tile(217);
        mansion[1][1] = new Tile(218);
        mansion[1][2] = new Tile(219);
        mansion[1][3] = new Tile(220);
        mansion[1][4] = new Tile(221);

        mansion[2][0] = new Tile(222);
        mansion[2][1] = new Tile(223);
        mansion[2][2] = new Tile(224, true);
        mansion[2][3] = new Tile(225);
        mansion[2][4] = new Tile(226);

        //mansion[4][3] = new Tile(34, true);

        return mansion;
    }

    public final static Tile[][] getMansionInsideTiles() {
        Tile[][] mansion = new Tile[Mansion.MANSION_HEIGHT][Mansion.MANSION_WIDTH];

        mansion[0][0] = new Tile(227);
        mansion[0][1] = new Tile(228);
        mansion[0][2] = new Tile(229);
        mansion[0][3] = new Tile(230);
        mansion[0][4] = new Tile(231);

        mansion[1][0] = new Tile(232);
        mansion[1][1] = new Tile(233);
        mansion[1][2] = new Tile(234);
        mansion[1][3] = new Tile(235);
        mansion[1][4] = new Tile(236);

        mansion[2][0] = new Tile(237);
        mansion[2][1] = new Tile(238);
        mansion[2][2] = new Tile(239, true);
        mansion[2][3] = new Tile(240);
        mansion[2][4] = new Tile(241);

        //mansion[4][3] = new Tile(34, true);

        return mansion;
    }


}