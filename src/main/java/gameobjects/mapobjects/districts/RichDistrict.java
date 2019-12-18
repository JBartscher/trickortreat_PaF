package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class RichDistrict extends District {

    public RichDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 1;
        this.houseColorKey = 6;
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
