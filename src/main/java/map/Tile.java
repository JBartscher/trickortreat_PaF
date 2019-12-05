package main.java.map;

public class Tile {
    boolean walkable;
    int tileNr;
    public static final int TILE_SIZE = 64;

    public Tile(int n, boolean walkable) {
        this.tileNr = n;
        this.walkable = walkable;
    }

    public int getTileNr() {
        return this.tileNr;
    }
}
