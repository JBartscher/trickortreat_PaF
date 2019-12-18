package main.java.map;//TODO: Ich check Enums nicht :( Wenn jemand das durch einen Enum ersetzten könnte wäre das Töfte.

import main.java.gameobjects.mapobjects.TownHall;

/**
 * The TileCollection holds all possible Tiles as a static reference.
 */
public class TileCollection {

    private final static int HOUSE = 5;
    public final static Tile HOUSE_TILE = new Tile(HOUSE);
    private final static int DOOR = 8;
    public final static Tile DOOR_TILE = new Tile(DOOR);
    private final static int GRASS = 1;
    public final static Tile GRASS_TILE = new Tile(GRASS, true);
    private final static int STREET = 2;
    public final static Tile STREET_TILE = new Tile(STREET, true);

    public static Tile[][] getSmallHouseUnvisitedTiles() {
        Tile[][] smallHouse = new Tile[2][2];
        smallHouse[0][0] = new Tile(51);
        smallHouse[0][1] = new Tile(52);

        smallHouse[1][0] = new Tile(53);
        smallHouse[1][1] = new Tile(54, true);
        return smallHouse;
    }

    public final static Tile[][] getSmallHouseVisitedTiles() {
        Tile[][] smallHouse = getSmallHouseUnvisitedTiles();
        smallHouse[1][0] = new Tile(51);
        return smallHouse;
    }

    public static Tile[][] getBigHouseUnvisitedTiles() {
        Tile[][] bigHouse = new Tile[2][3];
        bigHouse[0][0] = new Tile(61);
        bigHouse[0][1] = new Tile(62);
        bigHouse[0][2] = new Tile(63);

        bigHouse[1][0] = new Tile(64);
        bigHouse[1][1] = new Tile(65, true);
        bigHouse[1][2] = new Tile(66);

        return bigHouse;
    }

    public final static Tile[][] getBigHouseVisitedTiles() {
        Tile[][] bigHouse = getBigHouseUnvisitedTiles();
        bigHouse[1][0] = new Tile(61);
        bigHouse[1][2] = new Tile(63);
        return bigHouse;
    }

    public final static Tile[][] getTownHallTiles() {
        Tile[][] townHall = new Tile[TownHall.TOWN_HALL_HEIGHT][TownHall.TOWN_HALL_WIDTH];
        townHall[0][0] = new Tile(61);
        townHall[0][1] = new Tile(62);
        townHall[0][2] = new Tile(63);
        townHall[0][3] = new Tile(64);
        townHall[0][4] = new Tile(64);
        townHall[0][5] = new Tile(64);

        townHall[1][0] = new Tile(61);
        townHall[1][1] = new Tile(62);
        townHall[1][2] = new Tile(63);
        townHall[1][3] = new Tile(64);
        townHall[1][4] = new Tile(64);
        townHall[1][5] = new Tile(64);

        townHall[2][0] = new Tile(61);
        townHall[2][1] = new Tile(62);
        townHall[2][2] = new Tile(63);
        townHall[2][3] = new Tile(64);
        townHall[2][4] = new Tile(64);
        townHall[2][5] = new Tile(64);

        townHall[3][0] = new Tile(61);
        townHall[3][1] = new Tile(62);
        townHall[3][2] = new Tile(63, true);
        townHall[3][3] = new Tile(64, true);
        townHall[3][4] = new Tile(64);
        townHall[3][5] = new Tile(64);

        return townHall;
    }


}