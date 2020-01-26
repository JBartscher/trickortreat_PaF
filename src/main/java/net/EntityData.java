package main.java.net;

import main.java.Game;
import main.java.MovementManager;
import main.java.gameobjects.Entity;

import java.awt.*;
import java.io.Serializable;

/**
 * this class contains all entity data and is used in network games
 */
public class EntityData implements Serializable {
    private static final long serialVersionUID = 5611352444651877561L;

    protected double xPos;


    protected double yPos;
    protected double size;
    protected Point target;

    // represent horizontal and vertical move directions
    protected MovementManager.MoveDirection moveDirection = MovementManager.MoveDirection.DOWN;

    // used to control the animation speed of the movement animation
    protected int animCounter = 0;
    // represents the state of movement
    protected int moveCounter = 1;

    protected boolean noCollision = false;

    protected double speed = 200 * Game.FRAMES / 50;

    public EntityData(Entity entity) {
        xPos = entity.getxPos();
        yPos = entity.getyPos();
        size = entity.getSize();
        target = entity.getTarget();
        moveDirection = entity.getMoveDirection();
        animCounter = entity.getAnimCounter();
        moveCounter = entity.getMoveCounter();
        speed = entity.getSpeed();
        noCollision = entity.isNoCollision();

    }


    public double getxPos() {
        return xPos;
    }

    public double getyPos() {
        return yPos;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public Point getTarget() {
        return target;
    }

    public MovementManager.MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public int getAnimCounter() {
        return animCounter;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isNoCollision() {
        return noCollision;
    }
}
