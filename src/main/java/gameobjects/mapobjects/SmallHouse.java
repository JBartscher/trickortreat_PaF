package main.java.gameobjects.mapobjects;

import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

public class SmallHouse extends House {
    public SmallHouse(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getSmallHouseTiles();
    }

    @Override
    public void setDistrict(District district) {
        super.setDistrict(district);

        this.tileset = district.getSmallHouseTileset();
    }
}
