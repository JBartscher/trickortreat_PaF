package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;

/**
 * Abstract class for Districts
 */
public abstract class District {

    protected double candy_multiplikator = 0;
    protected int houseColorKey = 5;
    private Sector sector;

    public District(Sector sector) {
        this.sector = sector;
    }

    public Sector getSector() {
        return sector;
    }

    public double getCandy_multiplikator() {
        return candy_multiplikator;
    }

    public abstract Tile[][] getSmallHouseUnvisitedTileset();

    public abstract Tile[][] getSmallHouseVisitedTileset();

    public abstract Tile[][] getBigHouseUnvisitedTileset();

    public abstract Tile[][] getBigHouseVisitedTileset();
}
