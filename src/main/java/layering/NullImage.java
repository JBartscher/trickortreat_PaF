package main.java.layering;

import javafx.scene.image.Image;
import main.java.Singleton;


/**
 * empty Image object, which is the default image for a decorator layer image.
 * This class implements the null object pattern.
 */
public class NullImage extends Image implements Singleton {

    private static NullImage instance;

    private NullImage() {
        super("");
    }

    public static NullImage getInstance() {
        if (NullImage.instance == null) {
            NullImage.instance = new NullImage();
        }
        return instance;
    }

}
