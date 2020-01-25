package main.java.map;

import main.java.MovementManager;
import main.java.gameobjects.Entity;
import main.java.gameobjects.Witch;

import java.util.ArrayList;
import java.util.List;

/**
 * A Sector is a bigger chunk of the map which holds placed Objects.
 * Its purpose is to prevent a loop through all placed objects if one
 * tries to place a new  object.
 */
public class Sector extends Placeable {

    //private ArrayList<Placeable> containingPlacebles;
    private final List<MapObject> sectorObjects;


    public Sector(int x, int y, int width, int height) {
        super(x, y, width, height, 0);
        sectorObjects = new ArrayList<>();
    }

    /**
     * checks every item in containingPlacebles if it intersects with the given placeble.
     *
     * @param placeable the placeble that is checked wherater it intersects with any item in the sector
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
         * when the entity is the witch then do not use collision detection with more currency
         */
        if(entity instanceof Witch) {
            Placeable pWitch = new Placeable(entity.getEntityPos().y, entity.getEntityPos().x, 1, 1, 0);
            return intersectsWithContainingItems(pWitch);
        }

        double xOffset = 0;
        double yOffset = 0;


        /** improve collision with obstacles when moving in left direction
         *
         */
        if(entity.getMoveDirection() == MovementManager.MoveDirection.LEFT) {
            xOffset = -0.33;
        }

        if( (entity.getMoveDirection() == MovementManager.MoveDirection.DOWN) ) {
                //yOffset = -1;
        }

        Placeable placeable = new Placeable(entity.getEntityPosWithCurrency(yOffset).y, entity.getEntityPosWithCurrency(xOffset).x, 1, 1, 0);


        for (Placeable p : sectorObjects) {

            if (p.intersects(placeable)) {

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
}
