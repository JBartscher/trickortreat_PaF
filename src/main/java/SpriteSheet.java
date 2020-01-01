package main.java;

import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import main.java.map.Tile;

public class SpriteSheet {

    private Image sprite;
    private Image[][] spriteSet;

    public SpriteSheet(String path, int directions, int amountOfMovements) {
        spriteSet = new Image[directions][amountOfMovements];

        try {
            sprite = new Image(SpriteSheet.class.getResourceAsStream("player.png"));

        } catch(Exception e){
            e.printStackTrace();

        }

        for(int vertical = 0; vertical < directions; ++vertical) {
            for(int horizontal = 0; horizontal < amountOfMovements; ++horizontal) {
                spriteSet[vertical][horizontal] = new WritableImage(sprite.getPixelReader(),
                        horizontal * Tile.TILE_SIZE, vertical * Tile.TILE_SIZE,
                        Tile.TILE_SIZE, Tile.TILE_SIZE);
            }
        }
    }

    public Image getSpriteImage(int x, int y){
        return spriteSet[y][x];
    }

}
