package map.placing_utils;

import map.MapObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A Sector is a bigger chunk of the map which holds placed Objects.
 * Its purpose is to prevent a loop through all placed objects if one
 * tries to place a new  object.
 */
public class Sector extends Placeble {

    private ArrayList<Placeble> containingPlacebles;

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
    public boolean intersectsWithContainingItems(Placeble placeble) {
        for (Placeble p : containingPlacebles) {
            if (p.intersects(placeble))
                return true;
        }
        return false;
    }

    /**
     * adds a new placeble to the sector.
     *
     * @param placeble
     */
    public void addPlaceable(Placeble placeble) {
        if (!intersectsWithContainingItems(placeble))
            containingPlacebles.add(placeble);
    }

    List<MapObject> sectorObjects;

    /**
     * returns the list of all placed mapObjects which are within this district
     *
     * @return a List of all MapObjects which are part of this district.
     */
    List<MapObject> getAllContainingMapObjects() {
        return this.sectorObjects;
    }
}