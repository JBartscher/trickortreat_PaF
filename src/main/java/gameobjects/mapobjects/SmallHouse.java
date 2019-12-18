package main.java.gameobjects.mapobjects;

import main.java.map.Placeable;
import main.java.map.TileCollection;

public class SmallHouse extends House {
    public SmallHouse(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getSmallHouseTiles();
    }

    public SmallHouse(Placeable placeable) {
        super(placeable);
        this.tileset = TileCollection.getSmallHouseTiles();
    }
}
