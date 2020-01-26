package main.java.map;

import main.java.MovementManager;
import main.java.gameobjects.Entity;
import main.java.gameobjects.Witch;
import main.java.pattern.Composite;

import java.util.ArrayList;
import java.util.List;

/**
 * A Sector is a bigger chunk of the map which holds placed Objects.
 * Its purpose is to prevent a loop through all placed objects if one
 * tries to place a new  object.
 * Sector implements the Composite Pattern.
 */
public class Sector extends MapObject implements Composite<Sector> {

    /**
     * mapObjects that are contained by this sector
     */
    private final List<MapObject> sectorObjects;
    /**
     * collection of all child sectors of this sector
     */
    private final List<Sector> childSectors = new ArrayList<Sector>();
    /**
     * reference to the parent sector of this sector
     */
    private Sector parent = null;

    /**
     * sector constructor
     *
     * @param x x position
     * @param y y position
     * @param width width of sector
     * @param height height of sector
     */
    public Sector(int x, int y, int width, int height) {
        super(x, y, width, height, 0);
        sectorObjects = new ArrayList<>();

    }

    @Override
    public Tile getTileByTileIndex(int x, int y) {
        return null;
    }

    /**
     * sector constructor with parent reference
     *
     * @param x x position
     * @param y y position
     * @param width width of sector
     * @param height height of sector
     * @param parent parent sector
     */
    public Sector(int x, int y, int width, int height, Sector parent) {
        super(x, y, width, height, 0);
        sectorObjects = new ArrayList<>();
        this.parent = parent;
    }

    /**
     * checks every item in containingPlacebles if it intersects with the given placeble.
     *
     * @param placeable the placeble that is checked if it intersects with any item in the sector
     * @return true if the object intersects with other objects
     */
    public boolean intersectsWithContainingItems(Placeable placeable) {

        for (Placeable p : sectorObjects) {

            if (p.intersects(placeable)) {

                return true;
            }
        }
        return false;
    }

    /**
     * checks every item in containingPlacebles if it intersects with the given entity.
     *
     * @param entity that could collide with other objects
     * @return true if the object intersects with other objects
     */
    public boolean entityIntersectsWithContainingItems(Entity entity) {

        /**
          when the entity is the witch then do not use collision detection with more currency
         */
        if(entity instanceof Witch) {
            Placeable pWitch = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);
            return intersectsWithContainingItems(pWitch);
        }

        /**
         * use an offset to create collision detection with more currency in when necessary
         */
        double xOffset = 0;
        double yOffset = 0;

        /**
         * this local variable represents a building that gets a little offset in few situations to ensure better collisions
         */
        Placeable placeableLeft = null;


        /**
         * improve collision detection with buildings when moving left
         */
        if(entity.getMoveDirection() == MovementManager.MoveDirection.LEFT) {
            xOffset = -0.33;
        }


        /**
         * improve collision detection with buildings when moving up or down
         * also uses a second variable (placeableLeft) to invert the offset (to ensure also a better collision detection when walking from left to right
         */
        if( (entity.getMoveDirection() == MovementManager.MoveDirection.DOWN || entity.getMoveDirection() == MovementManager.MoveDirection.UP ) ) {
                xOffset = -0.33;
                placeableLeft = new Placeable(entity.getEntityPosWithCurrency(yOffset).y, entity.getEntityPosWithCurrency(+0.15).x, 1, 1, 0);
        }

        Placeable placeable = new Placeable(entity.getEntityPosWithCurrency(yOffset).y, entity.getEntityPosWithCurrency(xOffset).x, 1, 1, 0);


        /**
         * check if the entity collided with a building
         */
        for (Placeable p : sectorObjects) {

            if (p.intersects(placeable) || (placeableLeft != null && p.intersects(placeableLeft) )) {

                return true;
            }
        }
        return false;
    }

    /**
     * adds a new mapObject to the sector.
     *
     * @param mapObject the new MapObject that is appended to the list of MapObjects in this Sector
     */
    public void addMapObject(MapObject mapObject) {
        sectorObjects.add(mapObject);
        /*
          if this sector is a child of another sector, the mapObject is also added to his list of mapObjects.
         */
        if(this.parent != null){
            parent.addMapObject(mapObject);
        }
    }

    /**
     * returns the list of all placed mapObjects which are within this district
     *
     * @return a List of all MapObjects which are part of this district.
     */
    public List<MapObject> getAllContainingMapObjects() {
        return this.sectorObjects;
    }

    /**
     * removes a mapObject from the sector. This method is needed to ensure that the old BigHouse object, which is added
     * in the findObjectSpot method is not present in the sectorObject list.
     *
     * @param mapObject
     * @see MapGenerator->createHouses()->createWitchHouse
     */
    public void removeMapObject(MapObject mapObject) {
        sectorObjects.remove(mapObject);
    }


    /**
     * remove a childSector from this Sector and all its MapObjects.
     *
     * @param child the childSector that shall be removed
     */
    @Override
    public void removeChild(Sector child) {
        this.childSectors.remove(child);
        child.getAllContainingMapObjects().forEach(mapObject -> this.removeMapObject(mapObject));
    }
    /**
     * add a Sector to this Sectors child list.
     *
     * Sets the parent reference of the child to this Sector.
     *
     * @param child the sector that gets added to this sector
     */
    @Override
    public void addChild(Sector child) {
        this.childSectors.add(child);
        this.sectorObjects.addAll(child.sectorObjects);
        child.setParent(this);
    }

    /**
     * set the parentReference of this Sector.
     *
     * @param parent the new parentSector
     */
    @Override
    public void setParent(Sector parent) {
        this.parent = parent;
    }

    /**
     * get the entire list of child objects of this sector.
     *
     * @return a list of all child sectors of this sector
     */
    @Override
    public List<Sector> getChildren() {
        return this.childSectors;
    }
}
