package main.java.map;

import javafx.scene.image.Image;
import main.java.layering.DecoratorLayer;
import main.java.layering.Drawable;
import main.java.sprites.GraphicsUtility;

import java.io.Serializable;

/**
 * class that stores a tile
 */
public class Tile extends Drawable implements Serializable {
    private static final long serialVersionUID = 4862360600686727902L;

    boolean isDoorTile;

    /**
     * Tile size (px)
     */
    public static final int TILE_SIZE = 64;

    public Tile(int nr, boolean isDoorTile) {
        this.nr = nr;
        this.isDoorTile = isDoorTile;
        this.image = GraphicsUtility.getTileImage(nr);

        baseLayer = new DecoratorLayer();
        objectLayer = new DecoratorLayer();
        coverLayer = new DecoratorLayer();
    }

    public Tile(int nr) {
        this.nr = nr;
        this.isDoorTile = false;
        this.image = GraphicsUtility.getTileImage(nr);

        baseLayer = new DecoratorLayer();
        objectLayer = new DecoratorLayer();
        coverLayer = new DecoratorLayer();
    }


    public boolean isDoorTile() {
        return this.isDoorTile;
    }

    /**
     * ab jetzt verboten! Immer neues Tile anlegen!
     * <p>
     * public void setTileNr(int tileNr) {s
     * this.tileNr = tileNr;
     * }
     */

    public int getTileNr() {
        return this.nr;
    }

    @Override
    public Image getImage() {
        if (this.image != null) {
            return this.image;
        } else {
            return GraphicsUtility.getTileImage(0);
        }
    }
}
