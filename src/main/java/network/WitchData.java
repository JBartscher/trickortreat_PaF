package main.java.network;

import main.java.gameobjects.Witch;

import java.awt.*;

/**
 * this class contains all witch data and is used in network games
 */
public class WitchData extends EntityData {

    private double homeX;
    private double homeY;
    private boolean onReturn;

    private Point finalTargetPos;

    public WitchData(Witch witch) {
        super(witch);

        homeX = witch.getHomeX();
        homeY = witch.getHomeY();
        onReturn = witch.isOnReturn();
        this.finalTargetPos = witch.getFinalTargetPos();

    }

    public double getHomeX() {
        return homeX;
    }

    public double getHomeY() {
        return homeY;
    }

    public boolean isOnReturn() {
        return onReturn;
    }

    public Point getFinalTargetPos() {
        return finalTargetPos;
    }

}
