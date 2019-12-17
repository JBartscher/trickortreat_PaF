package main.java;

import main.java.map.Tile;

import java.awt.*;

import static java.lang.Math.round;

public abstract class Entity {

    protected double xPos;
    protected double yPos;
    protected double size;

    // Default-Werte
    protected Entity() {
        xPos = 0;
        yPos = 0;
        size = Tile.TILE_SIZE;
    }

    public double getxPos() {
        return xPos;
    }

    public void setxPos(double xPos) {
        this.xPos = xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public void setyPos(double yPos) {
        this.yPos = yPos;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Point getEntityPos() {
        return new Point((int) round(xPos / Tile.TILE_SIZE), (int) round(yPos / Tile.TILE_SIZE));
    }

}
