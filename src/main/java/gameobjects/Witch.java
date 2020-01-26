package main.java.gameobjects;

import main.java.net.WitchData;
import main.java.map.Tile;
import main.java.sprites.SpriteSheet;

import java.awt.*;

import static java.lang.Math.round;

/**
 * this class contains the model data of the npc
 * the main difference to the super class is the addition of a list of targets and a home position
 * the list of targets is used to find a walkable path to the next player - the final target is the player or home position
 */
public class Witch extends Entity {

    private double homeX;
    private double homeY;
    private boolean onReturn;

    private Point finalTargetPos;


    public Witch(){
        this.homeX = xPos;
        this.homeY = yPos;
        this.speed = getSpeed() * 2;
        this.finalTargetPos = new Point(0, 0);
        this.setSpriteSheet(new SpriteSheet("witch_image.png", 4, 3));
        this.sprite = spriteSheet.getSpriteImage(0, 1);
    }

    /**
     * update the witch data when changes like collisions occurred
     * @param witchData
     */
    public void setGameStateData(WitchData witchData) {
        super.setGameStateData(witchData);
        this.homeX = witchData.getHomeX();
        this.homeY = witchData.getHomeY();
        this.onReturn = witchData.isOnReturn();
        this.finalTargetPos = witchData.getFinalTargetPos();
    }

    public double getHomeX() {
        return homeX;
    }

    public void setHomeX(double homeX) {
        this.homeX = homeX;
    }

    public double getHomeY() {
        return homeY;
    }

    public void setHomeY(double homeY) {
        this.homeY = homeY;
    }

    public boolean isOnReturn() {
        return onReturn;
    }

    public void setOnReturn(boolean onReturn) {
        this.onReturn = onReturn;
    }

    public Point getHomePos() {
        return new Point((int) round(homeX / Tile.TILE_SIZE), (int) round(homeY / Tile.TILE_SIZE));
    }

    public Point getFinalTargetPos() {
        return finalTargetPos;
    }

    public void setFinalTargetPos(Point finalTargetPos) {
        this.finalTargetPos = finalTargetPos;
    }
}
