package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;

public class TownHall extends House {

    private static final int TOWN_HALL_WIDTH = 4;
    private static final int TOWN_HALL_HEIGHT = 6;

    public TownHall(int x, int y) {
        super(x, y, TownHall.TOWN_HALL_HEIGHT, TownHall.TOWN_HALL_WIDTH);
    }

    @Override
    public void visit(Player player) {
        // super.visit(player);

        // release player
    }
}
