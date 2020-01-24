package main.java.layering;

import javafx.scene.image.Image;

public abstract class Drawable {

    /**
     * Layer which is used to store the floor and other "bottom level" objects
     */
    public Layer baseLayer;
    /**
     * Layer which is used to store decorative Stuff
     */
    public Layer objectLayer;
    public Layer coverLayer;
    protected Image image;
    protected int nr;

    public abstract Image getImage();

}
