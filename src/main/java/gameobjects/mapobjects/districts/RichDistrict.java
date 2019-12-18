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
    public Tile[][] getSmallHouseTileset() {
        return TileCollection.getSmallHouseTiles();
    }

    @Override
    public Tile[][] getBigHouseTileset() {
        return TileCollection.getBigHouseTiles();
    }


}
