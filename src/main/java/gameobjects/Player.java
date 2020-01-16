package main.java.gameobjects;

import javafx.scene.image.Image;
import main.java.Entity;
import main.java.MovementManager;
import main.java.Network.PlayerData;
import main.java.SpriteSheet;
import main.java.gameobjects.mapobjects.House;
import main.java.map.MapObject;

public class Player extends Entity {

    // represents amount of children
    public int childrenCount = 3;
    private int candy = 0;

    // necessary to get  the right coordinates when playing with mouse
    private int xOffSet, yOffSet;

    private boolean inside;
    private boolean noCollision;

    private boolean hasKey = true;
    private MapObject insideObject;

    // Nach einer Kollision mit einer Hexe ist ein Spieler eine Zeit lang geschützt, um Mehrfachkollisionen zu vermeiden
    private double protectedTicks;

    private SpriteSheet spriteSheet2;
    private Image sprite2;
    private SpriteSheet spriteSheet3;
    private Image sprite3;
    private SpriteSheet spriteSheet4;
    private Image sprite4;


    /**
     * constructor which creates a player Object with a specific MovementType
     *
     * @param type MovementType
     */
    public Player(MovementManager.MovementType type) {
        super();
        this.movementType = type;
        spriteSheet2 = new SpriteSheet("jacko.png", 4, 3);
        spriteSheet3 = new SpriteSheet("player.png", 4, 3);
        spriteSheet4 = new SpriteSheet("jacko.png", 4, 3);
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
        return childrenCount;
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

    public void setChildrenCount(int value) {
        this.childrenCount = value;
    }

    /**
     * makeshift method to make the gameState of a player object interchangeable between network players,
     * without sending large objects like images via network.
     *
     * @param playerData PlayerData
     */
    public void setGameStateData(PlayerData playerData) {
        super.setGameStateData(playerData);

        this.childrenCount = playerData.getChildrenCount();
        this.candy = playerData.getCandy();
        this.xOffSet = playerData.getxOffSet();
        this.yOffSet = playerData.getyOffSet();
        this.inside = playerData.isInside();
        this.noCollision = playerData.isNoCollision();
        this.insideObject = playerData.getInsideObject();
        this.protectedTicks = playerData.getProtectedTicks();
        this.hasKey = playerData.hasKey();
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

    public double getProtectedTicks() {
        return protectedTicks;
    }

    public void setProtectedTicks(double protectedTicks) {
        this.protectedTicks = protectedTicks;
    }

    public boolean hasKey() {
        return hasKey;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public Image getEntityImage2() {
        return sprite2;
    }

    public Image getEntityImage3() {
        return sprite3;
    }

    public Image getEntityImage4() {
        return sprite4;
    }

    public SpriteSheet getSpriteSheet2() {
        return spriteSheet2;
    }

    public void setSpriteSheet2(SpriteSheet spriteSheet2) {
        this.spriteSheet2 = spriteSheet2;
    }

    public SpriteSheet getSpriteSheet3() {
        return spriteSheet3;
    }

    public void setSpriteSheet3(SpriteSheet spriteSheet3) {
        this.spriteSheet3 = spriteSheet3;
    }

    public SpriteSheet getSpriteSheet4() {
        return spriteSheet4;
    }

    public void setSpriteSheet4(SpriteSheet spriteSheet4) {
        this.spriteSheet4 = spriteSheet4;
    }

    public void setEntityImage(boolean calledByNetworkContext) {
        super.setEntityImage(calledByNetworkContext);

        this.sprite2 = spriteSheet2.getSpriteImage(moveCounter, moveDirection.ordinal());
        this.sprite3 = spriteSheet3.getSpriteImage(moveCounter, moveDirection.ordinal());
        this.sprite4 = spriteSheet4.getSpriteImage(moveCounter, moveDirection.ordinal());


    }


}
