package main.java.network;

import main.java.gameobjects.Player;
import main.java.map.MapObject;

/**
 * this class contains all player data and is used in network games
 */
public class PlayerData extends EntityData {

    /**
     * represent the amount of children
     */
    public int childrenCount = 3;
    private int candy = 0;


    /**
     * necessary to determine the right coordinates when playing with mouse
     */
    private int xOffSet, yOffSet;

    private boolean inside;
    private MapObject insideObject;
    private boolean hasKey;

    private double protectedTicks;

    public PlayerData(Player player) {
        super(player);
        this.childrenCount = player.getChildrenCount();
        this.candy = player.getCandy();
        this.xOffSet = player.getxOffSet();
        this.yOffSet = player.getyOffSet();
        this.inside = player.isInside();
        boolean noCollision = player.isNoCollision();
        this.insideObject = player.getInsideObject();
        this.protectedTicks = player.getProtectedTicks();
        this.hasKey = player.hasKey();
    }


    public int getChildrenCount() {
        return childrenCount;
    }

    public int getCandy() {
        return candy;
    }

    public int getxOffSet() {
        return xOffSet;
    }

    public int getyOffSet() {
        return yOffSet;
    }

    public boolean isInside() {
        return inside;
    }

    public MapObject getInsideObject() {
        return insideObject;
    }

    public double getProtectedTicks() {
        return protectedTicks;
    }

    public boolean hasKey() {
        return hasKey;
    }


}
