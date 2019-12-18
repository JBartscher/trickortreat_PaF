package main.java.gameobjects.mapobjects;

import main.java.map.TileCollection;

public class BigHouse extends House {
    public BigHouse(int x, int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        this.tileset = TileCollection.getBigHouseTiles();
    }
}
