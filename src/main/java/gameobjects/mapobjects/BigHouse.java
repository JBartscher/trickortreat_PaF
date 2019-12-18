package main.java.gameobjects.mapobjects;

import main.java.map.Placeable;
import main.java.map.TileCollection;

public class BigHouse extends House {
    public BigHouse(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getBigHouseTiles();
    }

    public BigHouse(Placeable placeable) {
        super(placeable);
        this.tileset = TileCollection.getBigHouseTiles();
    }
}
