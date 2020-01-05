package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;

import java.io.Serializable;

/**
 * Abstract class for Districts
 */
public abstract class District implements Serializable {

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


    public enum BiomType {
        Gras, Sand, Desert, Snow;
    }

    public BiomType getBiomType() {
        return biomType;
    }

    public void setBiomType(BiomType biomType) {
        this.biomType = biomType;
    }

    private BiomType biomType;

    public abstract Tile[][] getSmallHouseUnvisitedTileset();

    public abstract Tile[][] getSmallHouseVisitedTileset();

    public abstract Tile[][] getBigHouseUnvisitedTileset();

    public abstract Tile[][] getBigHouseVisitedTileset();
}
