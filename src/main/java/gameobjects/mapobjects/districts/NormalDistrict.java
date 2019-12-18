package main.java.gameobjects.mapobjects.districts;

import main.java.map.Sector;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class NormalDistrict extends District {

    public NormalDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 2.5;
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
