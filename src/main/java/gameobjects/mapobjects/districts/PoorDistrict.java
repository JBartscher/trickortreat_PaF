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
    public Tile[][] getSmallHouseTileset() {
        return TileCollection.getSmallHouseTiles();
    }

    @Override
    public Tile[][] getBigHouseTileset() {
        return TileCollection.getBigHouseTiles();
    }
}
