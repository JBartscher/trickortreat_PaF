package map;

import gameobjects.District;

import java.awt.*;
import java.util.Arrays;

public class MapGridElement {

    private Cell[][] cells;
    private final int MAP_GRID_SIZE = 25; //uneven to center the street entries
    private District district;

    public MapGridElement(District district) {
        cells = new Cell[MAP_GRID_SIZE][MAP_GRID_SIZE];
        Arrays.fill(cells, new DefaultCell(this));

        this.district = district; //poor, civilian, rich
    }

    int getSize() {
        return this.cells.length;
    }

    /**
     * retuns a Point at whic h the Cell can be found in this MapGridElement
     *
     * @param cell cell of which the Point in the MapGridElement is returned
     * @return Point in Array
     */
    public Point getCellPosition(Cell cell) {
        return new Point(getRowIndexOfCell(cell), getColumnIndexOfCell(cell));
    }

    /**
     * returns the row position in the MapGridElement 2D Array
     *
     * @param cell cell of which the row index is returned
     * @return row index
     */
    public int getRowIndexOfCell(Cell cell) {
        int row_index = Arrays.asList(cells).indexOf(cell);
        return row_index;
    }

    /**
     * returns the column position in the MapGridElement 2D Array
     *
     * @param cell cell of which the column index is returned
     * @return column index
     */
    public int getColumnIndexOfCell(Cell cell) {
        Cell[] cell_row = Arrays.asList(cells).get(getRowIndexOfCell(cell));
        int column_index = Arrays.asList(cell_row).indexOf(cell);
        return column_index;
    }
}
