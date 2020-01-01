package main.java.map;

import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.Properties;

public class Tile implements Serializable {
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

    /**
     * return Color of Tile
     *
     * @return Color of Tile
     */
    public Color getTileColor() {
        return (Color) tileColors.getOrDefault(this.tileNr, Color.WHITE);
    }
}
