package map;

import gameobjects.Street;

public class StreetCell extends DefaultCell {

    private Street street;

    StreetCell(MapGridElement gridElement, Street street) {
        super(gridElement);
        this.street = street;
    }

}
