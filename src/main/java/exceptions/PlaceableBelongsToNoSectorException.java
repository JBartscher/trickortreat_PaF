package main.java.exceptions;

/**
 * Object from type Placeable is not belonging to any sector exception
 */
public class PlaceableBelongsToNoSectorException extends Exception {
    public PlaceableBelongsToNoSectorException(String s) {
        super(s);
    }
}
