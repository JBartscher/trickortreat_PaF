package main.java;

import javafx.scene.image.Image;
import main.java.map.Tile;

import java.io.Serializable;

public class PlayerSnake implements Serializable {


    public double xPos;
    public double yPos;
    public Image image;
    private PlayerSnake nextChildren;


    public PlayerSnake(double xPos, double yPos, Image image, int count){
        this.xPos = xPos;
        this.yPos = yPos + Tile.TILE_SIZE;
        this.image = image;
        if(count > 1) {
            //System.out.println(count);
            this.nextChildren = new PlayerSnake(this.xPos, this.yPos, this.image, count - 1);
        }

    }


    public void transferImageToNext(Image image) {
        Image currentImage = this.image;
        this.image = image;
        if (getNext() != null) {
            getNext().transferImageToNext(currentImage);
        }
    }

    public void transferCoordinatesToNext(double xPos, double yPos) {


        double currentXPos = this.xPos;
        double currentYPos = this.yPos;
        this.xPos = xPos + Tile.TILE_SIZE * 0.5;
        this.yPos = yPos;

        if (getNext() != null)
            getNext().transferCoordinatesToNext(currentXPos, currentYPos);


    }


    public PlayerSnake getNext() {
        return nextChildren; }




}
