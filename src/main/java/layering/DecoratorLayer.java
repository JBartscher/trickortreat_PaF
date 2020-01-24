package main.java.layering;

import javafx.scene.image.Image;
import main.java.sprites.GraphicsUtility;

public class DecoratorLayer extends Layer {

    public DecoratorLayer() {
    }

    @Override
    public Image getImage() {
        return null;
    }

    @Override
    public void setImage(int nr) {
        this.nr = nr;
        this.image = GraphicsUtility.getTileImage(nr);
    }
}
