package map;

import gameobjects.House;

public class HouseCell extends DefaultCell {

    private House house;

    HouseCell(MapGridElement gridElement, House house) {
        super(gridElement);
        this.house = house;
    }

    @Override
    public boolean isWalkable() {
        return false;
    }

}
