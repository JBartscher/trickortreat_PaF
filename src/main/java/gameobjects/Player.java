package main.java.gameobjects;

import main.java.Entity;
import main.java.MovementManager;
import main.java.Network.PlayerData;
import main.java.PlayerSnake;
import main.java.gameobjects.mapobjects.House;

import java.util.List;
import java.util.Stack;

public class Player extends Entity {

    transient public PlayerSnake childrenSnake;
    // represents amount of children
    public int health = 3;

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

        this.childrenSnake = new PlayerSnake(this.xPos, this.yPos, this.sprite, health - 1);
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

    public PlayerSnake getNext()  { return childrenSnake; }


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

    public MovementManager.MovementType movementType;

    public MovementManager.MovementType getMovementType() {
        return movementType;
    }

    public void setMovementType(MovementManager.MovementType movementType) {
        this.movementType = movementType;
    }

    public PlayerSnake getChildrenSnake() {
        return childrenSnake;
    }

    public void setChildrenSnake(PlayerSnake childrenSnake) {
        this.childrenSnake = childrenSnake;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Stack getChildren() {
        return children;
    }

    public void setChildren(Stack children) {
        this.children = children;
    }

    public void setCandy(int candy) {
        this.candy = candy;
    }

    public void setGameStateData(PlayerData playerData) {
        super.setGameStateData(playerData);

        //this.childrenSnake = playerData.getChildrenSnake();
        this.health = playerData.getHealth();
        //this.children = playerData.getChildren();
        this.candy = playerData.getCandy();
        this.xOffSet = playerData.getxOffSet();
        this.yOffSet = playerData.getyOffSet();


    }


}
