package main.java.Network;

import main.java.gameobjects.Player;

import java.util.Stack;

public class PlayerData extends EntityData {

    // represents amount of children
    public int health = 3;

    private Stack children;

    private int candy = 0;

    //public PlayerSnake childrenSnake;

    // necessary to get  the right coordinates when playing with mouse
    private int xOffSet, yOffSet;

    public PlayerData(Player player) {
        super(player);
        //this.childrenSnake = player.getChildrenSnake();
        this.health = player.getHealth();
        //this.children = player.getChildren();
        this.candy = player.getCandy();
        this.xOffSet = player.getxOffSet();
        this.yOffSet = player.getyOffSet();

    }



    /*
    public PlayerSnake getChildrenSnake() {
        return childrenSnake;
    }

    public void setChildrenSnake(PlayerSnake childrenSnake) {
        this.childrenSnake = childrenSnake;
    }

     */

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
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







}
