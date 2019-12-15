package main.java.map;

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
     * @param placeble the placeble that is checked wherater it intersects with any item in the sector
     * @return true if the object intersects with other objects
     */
    public boolean intersectsWithContainingItems(Placeable placeble) {
        for (Placeable p : sectorObjects) {
            if (p.intersects(placeble))
                return true;
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
}
