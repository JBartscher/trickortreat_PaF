package map;

import java.awt.*;

public interface Cell {
    /**
     * a cell is the smallest unit of the overall game map.
     * <p>
     * it is a square of 256x256 px.
     */

    boolean isWalkable();

    Point getPosition();
}
