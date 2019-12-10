package main.java.map;

import java.util.ArrayList;
import java.util.List;

/**
 * A Sector is a bigger chunk of the map which holds placed Objects.
 * Its purpose is to prevent a loop through all placed objects if one
 * tries to place a new  object.
 */
public class Sector extends Placeable {

    private ArrayList<Placeable> containingPlacebles;
    private List<MapObject> sectorObjects;

    public Sector(int x, int y, int width, int height) {
        super(x, y, width, height, 0);
        containingPlacebles = new ArrayList<>();
    }

    /**
     * checks every item in containingPlacebles if it intersects with the given placeble.
     *
     * @param placeble
     * @return true if the object intersects with other objects
     */
    public boolean intersectsWithContainingItems(Placeable placeble) {
        for (Placeable p : containingPlacebles) {
            if (p.intersects(placeble))
                return true;
        }
        return false;
    }

    /**
     * adds a new placeble to the sector.
     *
     * @param placeable
     */
    public void addPlaceable(Placeable placeable) {
        if (!intersectsWithContainingItems(placeable))
            containingPlacebles.add(placeable);
    }

    /**
     * returns the list of all placed mapObjects which are within this district
     *
     * @return a List of all MapObjects which are part of this district.
     */
    List<MapObject> getAllContainingMapObjects() {
        return this.sectorObjects;
    }
}
