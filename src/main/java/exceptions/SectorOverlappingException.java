package main.java.exceptions;

/**
 * Exception to identify Sector overlapping troubles
 */
public class SectorOverlappingException extends Exception {
    public SectorOverlappingException(String s) {
        super(s);
    }
}