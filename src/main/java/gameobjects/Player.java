package main.java.gameobjects;

import main.java.Entity;
import main.java.MovementManager;
import main.java.gameobjects.mapobjects.House;

import java.util.List;
import java.util.Stack;

public class Player extends Entity {

    public MovementManager.MovementType movementType;
    private Stack children;

    private int candy = 0;

    // necessary to get  the right coordinates when playing with mouse
    private int xOffSet, yOffSet;

    public Player(List children, MovementManager.MovementType type) {
        super();
        this.movementType = type;
        this.children = new Stack();
        this.children.addAll(children);
    }

    public Player(MovementManager.MovementType type)
    {
        super();
        //TODO: BEHELFSMÄSSIG SOLANGE ES NOCH KEINE MOVEMENT-QUEUE GIBT!
        this.movementType = type;
        children = new Stack();
        children.add(new Object());
        children.add(new Object());
        children.add(new Object());
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




    /**
     * returns the number of kids on the children stack. This is needed to calculate how much candy a player gets when
     * he visits the a house.
     * The maximum number of children is 3. The Minimum 1.
     *
     * @return size of children stack
     * @see House#visit(Player)
     */
    public int getChildrenCount() {
        return children.size();
    }

    /**
     * returns the amount of candy the player posses
     *
     * @return amount of candy
     */
    public int getCandy() {
        return candy;
    }

    /**
     * adds candy to the player candy count eg. his score
     *
     * @param candy the number of candys that is added to the candy score.
     */
    public void addCandy(int candy) {
        this.candy += candy;
    }


}
