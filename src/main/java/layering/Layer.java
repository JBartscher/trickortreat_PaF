package main.java.layering;

/**
 * The Decorator for Drawable Component
 */
public abstract class Layer extends Drawable {

    public abstract void setImage(int nr);

    public int getImageNr() {
        return this.nr;
    }
}
