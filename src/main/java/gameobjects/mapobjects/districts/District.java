package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;
import main.java.map.TileCollection;

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

    /**
     * biome type
     */
    private BiomeType biomeType;

    /**
     * get candy multiplikator
     *
     * @return candy multiplication
     */
    public double getCandyMultiplikator() {
        return candy_multiplikator;
    }

    /**
     * get type of biome.
     *
     * @return type of biome
     */
    public BiomeType getBiomeType() {
        return biomeType;
    }

    /**
     * set biome type
     *
     * @param biomeType the type of biome that will be settet to this district
     */
    public void setBiomeType(BiomeType biomeType) {
        this.biomeType = biomeType;
    }

    /**
     * method to retrive a small house unvisited tileset
     * @return small house unvisited tileset
     */
    public abstract Tile[][] getSmallHouseUnvisitedTileset();

    /**
     /**
     * method to retrieve a small house visited tileset
     * @return small house unvisited tileset
     */
    public abstract Tile[][] getSmallHouseVisitedTileset();

    /**
     * method to retrieve a big house unvisited tileset
     * @return big house unvisited tileset
     */
    public abstract Tile[][] getBigHouseUnvisitedTileset();

    /**
     * method to retrieve a big house visited tileset
     * @return big house visited tileset
     */
    public abstract Tile[][] getBigHouseVisitedTileset();

    /**
     * get unvisited gingerbreadhouse tileset.
     *
     * @return unvisited gingerbreadtileset
     */
    public Tile[][] getGingerbreadHouseUnvisitedTileset(){
        return TileCollection.getGingerbreadHouseUnvisitedTiles();
    }

    /**
     * get visited gingerbreadhouse tileset.
     *
     * @return visited gingerbreadtileset
     */
    public Tile[][] getGingerbreadHouseVisitedTileset(){
        return TileCollection.getGingerbreadHouseVisitedTiles();
    }

    /**
     * Biome enumeration
     */
    public enum BiomeType {
        Gras, Earth, Desert, Snow
    }

}
