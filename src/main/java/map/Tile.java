package main.java.map;

import java.io.Serializable;
import java.util.Properties;

/**
 * class that stores a tile
 */
public class Tile implements Serializable {
    private static final long serialVersionUID = 4862360600686727902L;
    boolean isDoorTile;
    int tileNr;
    static final Properties tileColors = new Properties();
    public static final int TILE_SIZE = 64;


    public Tile(int n, boolean isDoorTile) {
        this.tileNr = n;
        this.isDoorTile = isDoorTile;
    }

    public Tile(int n) {
        this.tileNr = n;
        this.isDoorTile = false;
    }

    public boolean isDoorTile() {
        return this.isDoorTile;
    }

    public void setTileNr(int tileNr) {
        this.tileNr = tileNr;
    }

    public int getTileNr() {
        return this.tileNr;
    }
}

