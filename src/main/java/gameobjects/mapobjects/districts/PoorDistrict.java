package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class PoorDistrict extends District {

    public PoorDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 1.5;
        this.houseColorKey = 7;
    }

    @Override
    public Tile[][] getSmallHouseUnvisitedTileset() {
        return TileCollection.getSmallHouseUnvisitedTiles();
    }

    @Override
    public Tile[][] getSmallHouseVisitedTileset() {
        return TileCollection.getSmallHouseVisitedTiles();
    }

    @Override
    public Tile[][] getBigHouseUnvisitedTileset() {
        return TileCollection.getBigHouseUnvisitedTiles();
    }

    @Override
    public Tile[][] getBigHouseVisitedTileset() {
        return TileCollection.getBigHouseVisitedTiles();
    }
}
