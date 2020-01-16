package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.map.MapObject;
import main.java.map.Tile;

public class Key extends MapObject {

    private boolean available;

    public Key(int x, int y, int width, int height, int offset) {
        super(x, y, width, height, offset);
    }

    public Key(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public Tile getTileByTileIndex(int x, int y) {
        return new Tile(1000);
    }

    public void collect(Player player){
        player.setHasKey(true);
        available = false;

    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
