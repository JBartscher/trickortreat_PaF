package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;

public class HouseDecorator extends House {

    /**
     * Component Object which will be decorated
     */
    protected House house;

    public HouseDecorator(House house) {
        super();
        this.house = house;
    }

    @Override
    public void visit(Player player) {
        house.visit(player);
    }

    @Override
    public void repaintAfterVisit() {

    }

    public House getDecoratedHouse() {
        return this.house;
    }
}
