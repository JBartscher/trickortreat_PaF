package main.java.gameobjects;

import javafx.scene.image.Image;
import main.java.Game;
import main.java.menu.GameMenu;
import main.java.MovementManager;
import main.java.network.EntityData;
import main.java.pattern.Observable;
import main.java.map.Tile;
import main.java.sprites.SpriteSheet;

import java.awt.*;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

import static java.lang.Math.round;

/**
 * the super class of every object with a movement ability
 * assume that these objects have a x/y position, movement target, size, sprite, direction and movement speed
 */
public abstract class Entity extends Observable implements Serializable {
    /**
     * Entity is now extending Observable, because Player Objects which inherit Entity
     * are now Observed by the GameMenu class.
     *
     * @see GameMenu#getFirstPlayerObserver()
     * @see GameMenu#getSecondPlayerObserver()
     */

    public double xPos;
    public double yPos;
    protected double size;
    public Point target;

    protected CopyOnWriteArrayList<Point> targets = new CopyOnWriteArrayList<>();

    protected boolean noCollision = false;

    transient protected SpriteSheet spriteSheet;
    transient protected Image sprite;

    // represent horizontal and vertical move directions
    protected MovementManager.MoveDirection moveDirection = MovementManager.MoveDirection.DOWN;

    // used to control the animation speed of the movement animation
    protected int animCounter = 0;
    // represents the state of movement
    protected int moveCounter = 1;

    public double speed = 300 * Game.FRAMES / 50 * 5;


    /**
     * constructor of an entity object
     * abstract class for player and witch object
     */
    protected Entity() {
        xPos = 0;
        yPos = 0;
        size = Tile.TILE_SIZE;
        target = new Point((int) xPos, (int) yPos);
        this.spriteSheet = new SpriteSheet("player.png", 4, 3);
        this.sprite = spriteSheet.getSpriteImage(0, 1);

    }

    // Default-Werte
    protected Entity(int speed) {
        this.speed = speed;
        xPos = 0;
        yPos = 0;
        size = Tile.TILE_SIZE;
        target = new Point((int) xPos, (int) yPos);
        this.spriteSheet = new SpriteSheet("player.png", 4, 3);
        this.sprite = spriteSheet.getSpriteImage(0, 1);
    }


    public Point getTarget() {
        return target;
    }

    public void setTarget(Point target) {
        this.target = target;
    }

    public void setSpriteSheet(SpriteSheet spriteSheet) {
        this.spriteSheet = spriteSheet;
    }

    public Image getSprite() {
        return sprite;
    }

    public void setSprite(Image sprite) {
        this.sprite = sprite;
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

    public void setSpeed(double speed) {
        this.speed = speed;
    }


    public void move() {

    }

    public void setTarget(double x, double y) {
        target.x = (int) x;
        target.y = (int) y;
    }

    public void setInput() {
    }

    public double getSpeed() {
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

    // Ermöglicht einen weicheren Übergang beim Checken der Kollision der Außengrenzen wegen Offset (Abstandsregelung)
    public Point getEntityPosWithCurrency(double offset) {
        offset = Tile.TILE_SIZE * offset;
        return new Point((int) round((xPos + offset) / Tile.TILE_SIZE), (int) round((yPos + offset) / Tile.TILE_SIZE));
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
    void changeMoveImages() {
        animCounter++;

        double movementSize = getSpeed() / Game.FRAMES;

        if (animCounter >= 5) {
            if (moveCounter == 2) {
                moveCounter = 0;
            } else {
                moveCounter++;
            }
            animCounter = 0;
        }

        // sofern am Ziel, dann Bild auf Stillstand setzen
        if (Math.abs(target.x - xPos) < movementSize && Math.abs(target.y - yPos) < movementSize) {
            moveCounter = 1;
        }
    }


    public void setEntityImage(boolean calledByNetworkContext) {
        if (!calledByNetworkContext) changeMoveImages();
        Image currentImage = this.sprite;
        this.sprite = spriteSheet.getSpriteImage(moveCounter, moveDirection.ordinal());

    }

    public Image getGameOverSpriteImage() {
        return spriteSheet.getSpriteImage(1, 0);
    }

    public boolean init = true;

    public void setGameStateData(EntityData entityData) {

        if (init) {
            init = false;
            this.xPos = entityData.getxPos();
            this.yPos = entityData.getyPos();
        } else {
            //this.xPos = entityData.getxPos();
            //this.yPos = entityData.getyPos();
            this.xPos = (entityData.getxPos() + xPos) / 2;
            this.yPos = (entityData.getyPos() + yPos) / 2;
        }

        this.size = entityData.getSize();
        this.target = entityData.getTarget();
        this.moveDirection = entityData.getMoveDirection();
        this.animCounter = entityData.getAnimCounter();
        this.moveCounter = entityData.getMoveCounter();
        this.speed = entityData.getSpeed();
        this.noCollision = entityData.isNoCollision();

    }

    public Image getEntityImage() {
        return sprite;
    }


    public SpriteSheet getSpriteSheet() {
        return spriteSheet;
    }

    public boolean isNoCollision() {
        return noCollision;
    }

    public void setNoCollision(boolean noCollision) {
        this.noCollision = noCollision;
    }

    public CopyOnWriteArrayList<Point> getTargets() {
        return targets;
    }

    public void setTargets(CopyOnWriteArrayList<Point> targets) {
        this.targets = targets;
    }

}
