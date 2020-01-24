package main.java.layering;

import javafx.scene.image.Image;

/**
 * The Decorator for Drawable Component
 */
public abstract class Layer extends Drawable {

    public abstract void setImage(Image image);

    public abstract void setImage(int nr);

    public int getImageNr() {
        return this.nr;
    }
}
