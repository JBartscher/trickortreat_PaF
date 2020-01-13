package main.java.Network;

import main.java.Entity;
import main.java.Game;
import main.java.MovementManager;

import java.awt.*;
import java.io.Serializable;

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

    public Point getTarget() {
        return target;
    }

    public void setTarget(Point target) {
        this.target = target;
    }

    public MovementManager.MoveDirection getMoveDirection() {
        return moveDirection;
    }

    public void setMoveDirection(MovementManager.MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    public int getAnimCounter() {
        return animCounter;
    }

    public void setAnimCounter(int animCounter) {
        this.animCounter = animCounter;
    }

    public int getMoveCounter() {
        return moveCounter;
    }

    public void setMoveCounter(int moveCounter) {
        this.moveCounter = moveCounter;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public boolean isNoCollision() {
        return noCollision;
    }

    public void setNoCollision(boolean noCollision) {
        this.noCollision = noCollision;
    }
}
