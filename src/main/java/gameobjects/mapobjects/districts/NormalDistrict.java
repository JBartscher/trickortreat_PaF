package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class NormalDistrict extends District {

    public NormalDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 4;
    }

    @Override
    public Tile[][] getSmallHouseUnvisitedTileset() {
        return TileCollection.getNormalSmallHouseUnvisitedTiles();
    }

    @Override
    public Tile[][] getSmallHouseVisitedTileset() {
        return TileCollection.getNormalSmallHouseVisitedTiles();
    }

    @Override
    public Tile[][] getBigHouseUnvisitedTileset() {
        return TileCollection.getNormalBigHouseUnvisitedTiles();
    }

    @Override
    public Tile[][] getBigHouseVisitedTileset() {
        return TileCollection.getNormalBigHouseVisitedTiles();
    }

}
