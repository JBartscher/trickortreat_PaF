package main.java.Network;

import main.java.gameobjects.Witch;

import java.awt.*;

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

    public Point getFinalTargetPos() {
        return finalTargetPos;
    }

    public void setFinalTargetPos(Point finalTargetPos) {
        this.finalTargetPos = finalTargetPos;
    }


}
