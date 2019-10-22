package map;

import java.awt.*;

public class DefaultCell implements Cell {

    private MapGridElement myMapGridElement;

    DefaultCell(MapGridElement gridElement) {
        this.myMapGridElement = gridElement;
    }

    public boolean isWalkable() {
        return true;
    }

    public Point getPosition() {
        return myMapGridElement.getCellPosition(this);
    }
}
