package main.java.map;

import javafx.scene.image.Image;

/**
 * Interface for Drawable Objects.
 */
public abstract class Drawable {

    protected Image image;
    protected int nr;

    public abstract Image getImage();
}
