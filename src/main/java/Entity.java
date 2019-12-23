package main.java;

import javafx.scene.image.Image;
import main.java.map.Tile;

import java.awt.*;

import static java.lang.Math.round;

;

public abstract class Entity {

    protected double xPos;
    protected double yPos;
    protected double size;
    protected Point target;

    protected SpriteSheet spriteSheet;
    protected Image sprite;

    // represent horizontal and vertical move directions
    protected MovementManager.MoveDirection moveDirection = MovementManager.MoveDirection.DOWN;

    // used to control the animation speed of the movement animation
    protected int animCounter = 0;
    // represents the state of movement
    protected int moveCounter = 1;

    protected int speed = 192;

    // Default-Werte
    protected Entity() {
        xPos = 0;
        yPos = 0;
        size = Tile.TILE_SIZE;
        target = new Point((int)xPos, (int)yPos);
        this.spriteSheet = new SpriteSheet("player.png", 4, 3);
        this.sprite = spriteSheet.getSpriteImage(0, 1);

    }

    // Default-Werte
    protected Entity(int speed) {
        this.speed = speed;
        xPos = 0;
        yPos = 0;
        size = Tile.TILE_SIZE;
        target = new Point((int)xPos, (int)yPos);
        this.spriteSheet = new SpriteSheet("player.png", 4, 3);
        this.sprite = spriteSheet.getSpriteImage(0, 1);
    }

    public void move() {

        /*
        double movementSize = speed / Game.FRAMES;

        if( (target.x - xPos) > 2) {
            xPos += movementSize ;
        } else if (target.x < xPos) {
            xPos -= movementSize ;
        }

        if( (target.y - yPos) > 2) {
            yPos += movementSize;
        } else if (target.y < yPos) {
            yPos -= movementSize;
        }


         */
    }

    public void setTarget(double x, double y) {
        target.x = (int)x;
        target.y = (int)y;
    }

    public void setInput(){
    }

    public int getSpeed() {
        return speed;
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

    public Point getEntityPos() {
        return new Point((int) round(xPos / Tile.TILE_SIZE), (int) round(yPos / Tile.TILE_SIZE));
    }


    // represents the direction in which a entity is moving / looking
    public MovementManager.MoveDirection getMoveDirection() {
        return moveDirection;
    }

    /**
     * get called by MovementManager
     * changes the movement direction to animate an entity correctly
     */
    public void setMoveDirection(MovementManager.MoveDirection moveDirection) {
        this.moveDirection = moveDirection;
    }

    // every 7 Frames the image of "movement" get replaced by the following image
    void changeMoveImages(){
        animCounter++;

        if(animCounter >= 7 ) {
            if(moveCounter == 2) {
                moveCounter = 0;
            } else {
                moveCounter++;
            }
            animCounter = 0;
        }

        // sofern am Ziel, dann Bild auf Stillstand setzen
        if(Math.abs(target.x - xPos) < 2 && Math.abs(target.y - yPos) < 2) {
            moveCounter = 1;
        }
    }


    protected void setEntityImage() {

        changeMoveImages();
        this.sprite = spriteSheet.getSpriteImage(moveCounter, moveDirection.ordinal());
    }

    protected Image getEntityImage() {
        return sprite;
    }

    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

}
