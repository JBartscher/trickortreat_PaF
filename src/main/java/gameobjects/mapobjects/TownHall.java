package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;
import main.java.map.Tile;
import main.java.map.TileCollection;

public class TownHall extends House implements Visitible {

    public static final int TOWN_HALL_WIDTH = 6;
    public static final int TOWN_HALL_HEIGHT = 4;

    public TownHall(int x, int y) {
        super(x, y, TownHall.TOWN_HALL_HEIGHT, TownHall.TOWN_HALL_WIDTH);
        this.tileset = TileCollection.getTownHallTiles();
    }

    @Override
    public void visit(Player player) {
        setInsideMode(player);

        notifyObservers(this);
    }

    @Override
    public void repaintAfterVisit() {

    }

    @Override
    public void setInsideMode(Player player) {
        if (!player.isInside()) {
            player.setNoCollision(true);
            player.setInside(true);
            player.setyPos(player.getyPos() + -Tile.TILE_SIZE);
            player.setInsideObject(this);

        } else {
            player.setNoCollision(false);
            player.setInside(false);
            player.setyPos(player.getyPos() + Tile.TILE_SIZE);
            player.setInsideObject(null);
        }

    }
}
