package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.map.TileCollection;

public class TownHall extends House {

    public static final int TOWN_HALL_WIDTH = 6;
    public static final int TOWN_HALL_HEIGHT = 4;

    public TownHall(int x, int y) {
        super(x, y, TownHall.TOWN_HALL_HEIGHT, TownHall.TOWN_HALL_WIDTH);
        this.tileset = TileCollection.getTownHallTiles();
    }

    @Override
    public void visit(Player player) {
        // super.visit(player);

        // release player

        notifyObservers(this);
    }

    @Override
    public void repaintAfterVisit() {

    }
}
