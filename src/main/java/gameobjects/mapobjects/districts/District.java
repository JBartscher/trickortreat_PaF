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

    public double getCandyMultiplikator() {
        return candy_multiplikator;
    }

    public enum BiomeType {
        Gras, Sand, Desert, Snow
    }

    public BiomeType getBiomeType() {
        return biomeType;
    }

    public void setBiomeType(BiomeType biomeType) {
        this.biomeType = biomeType;
    }

    private BiomeType biomeType;

    public abstract Tile[][] getSmallHouseUnvisitedTileset();

    public abstract Tile[][] getSmallHouseVisitedTileset();

    public abstract Tile[][] getBigHouseUnvisitedTileset();

    public abstract Tile[][] getBigHouseVisitedTileset();

    public abstract Tile[][] getGingerbreadHouseUnvisitedTileset();

    public abstract Tile[][] getGingerbreadHouseVisitedTileset();

}
