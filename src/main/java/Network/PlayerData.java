package main.java.Network;

import main.java.gameobjects.Player;
import main.java.map.MapObject;

import java.util.Stack;

public class PlayerData extends EntityData {

    // represents amount of children
    public int childrenCount = 3;

    private Stack children;

    private int candy = 0;

    //public PlayerSnake childrenSnake;

    // necessary to get  the right coordinates when playing with mouse
    private int xOffSet, yOffSet;

    private boolean inside;
    private boolean noCollision;
    private MapObject insideObject;

    public PlayerData(Player player) {
        super(player);
        //this.childrenSnake = player.getChildrenSnake();
        this.childrenCount = player.getChildrenCount();
        //this.children = player.getChildren();
        this.candy = player.getCandy();
        this.xOffSet = player.getxOffSet();
        this.yOffSet = player.getyOffSet();
        this.inside = player.isInside();
        this.noCollision = player.isNoCollision();
        this.insideObject = player.getInsideObject();
    }



    /*
    public PlayerSnake getChildrenSnake() {
        return childrenSnake;
    }

    public void setChildrenSnake(PlayerSnake childrenSnake) {
        this.childrenSnake = childrenSnake;
    }

     */

    public int getChildrenCount() {
        return childrenCount;
    }

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }

    /*
    public Stack getChildren() {
        return children;
    }

    public void setChildren(Stack children) {
        this.children = children;
    }

     */

    public int getCandy() {
        return candy;
    }

    public void setCandy(int candy) {
        this.candy = candy;
    }

    public int getxOffSet() {
        return xOffSet;
    }

    public void setxOffSet(int xOffSet) {
        this.xOffSet = xOffSet;
    }

    public int getyOffSet() {
        return yOffSet;
    }

    public void setyOffSet(int yOffSet) {
        this.yOffSet = yOffSet;
    }

    public boolean isInside() {
        return inside;
    }

    public void setInside(boolean inside) {
        this.inside = inside;
    }

    public boolean isNoCollision() {
        return noCollision;
    }

    public void setNoCollision(boolean noCollision) {
        this.noCollision = noCollision;
    }

    public MapObject getInsideObject() {
        return insideObject;
    }

    public void setInsideObject(MapObject insideObject) {
        this.insideObject = insideObject;
    }







}
