package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class RichDistrict extends District {

    public RichDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 0;
        this.houseColorKey = 6;
    }

    /**
     * get small, rich unvisited House tileset.
     *
     * @return TileArray for a small, rich unvisited House
     */
    @Override
    public Tile[][] getSmallHouseUnvisitedTileset() {
        return TileCollection.getRichSmallHouseUnvisitedTiles();
    }

    /**
     * get small, rich visited House tileset.
     *
     * @return TileArray for a small, rich visited House
     */
    @Override
    public Tile[][] getSmallHouseVisitedTileset() {
        return TileCollection.getRichSmallHouseVisitedTiles();
    }

    /**
     * get big, rich unvisited House tileset.
     *
     * @return TileArray for a big, rich unvisited House
     */
    @Override
    public Tile[][] getBigHouseUnvisitedTileset() {
        return TileCollection.getRichBigHouseUnvisitedTiles();
    }

    /**
     * get big, rich visited House tileset.
     *
     * @return TileArray for a small, big visited House
     */
    @Override
    public Tile[][] getBigHouseVisitedTileset() {
        return TileCollection.getRichBigHouseVisitedTiles();
    }


}
