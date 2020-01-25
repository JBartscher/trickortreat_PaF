package main.java.map;

import javafx.scene.image.Image;

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

/*
//TODO wie kriegen wir das im Netzwerk zum laufen?

package main.java.map;

import javafx.scene.image.Image;
import main.java.sprites.GraphicsUtility;

import java.io.Serializable;

public class Tile extends Drawable implements Serializable {
    private static final long serialVersionUID = 4862360600686727902L;

    boolean isDoorTile;

    public static final int TILE_SIZE = 64;


    public Tile(int nr, boolean isDoorTile) {
        this.nr = nr;
        this.isDoorTile = isDoorTile;
        //this.image = GraphicsUtility.getTileImage(nr);
    }

    public Tile(int nr) {
        this.nr = nr;
        this.isDoorTile = false;
        //this.image = GraphicsUtility.getTileImage(nr);
    }

    public boolean isDoorTile() {
        return this.isDoorTile;
    }

    public void setTileNr(int tileNr) {
        this.nr = tileNr;
        //this.image = GraphicsUtility.getTileImage(tileNr);
    }

    public int getTileNr() {
        return this.nr;
    }

    @Override
    public Image getImage() {
        return this.image;
    }
}
 */
