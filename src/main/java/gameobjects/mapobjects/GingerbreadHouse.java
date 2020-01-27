package main.java.gameobjects.mapobjects;

import main.java.pattern.Singleton;
import main.java.sounds.Sound;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

/**
 * represents the house of the npc
 */
public class GingerbreadHouse extends BigHouse {

    /**
     * Singleton
     */
    // static GingerbreadHouse instance = null;
    public static int PosX = 0;
    public static int PosY = 0;

    /**
     * boolean whether the house is holding a child
     */
    private boolean hasChild;

    private int children = 0;

    /**
     * private constructor to ensure the Singleton is not instantiated in any other places.
     *
     * @param tileWidth  width - see BigHouse.BIG_HOUSE_WIDTH
     * @param tileHeight height - see BigHouse.BIG_HOUSE_HEIGHT
     */
    public GingerbreadHouse(int x , int y, int tileWidth, int tileHeight) {
        super(x, y, tileWidth, tileHeight);
        GingerbreadHouse.PosX = x;
        GingerbreadHouse.PosY = y;
        System.out.println( " x: "+GingerbreadHouse.PosX + " y: "+ GingerbreadHouse.PosY);
        tileset = TileCollection.getGingerbreadHouseUnvisitedTiles();
    }

    /**
     * get unvisited tiles for the GingerbreadHouse
     *
     * @param district district (always normalDistrict)
     */
    @Override
    public void setDistrict(District district) {
        super.setDistrict(district);

        this.tileset = district.getGingerbreadHouseUnvisitedTileset();
    }

    /**
     * on visit catches one of the players kids that visits or releases one imprisoned child if the player posses a key.
     *
     * @param player the player entity that visits the current house instance
     */
    @Override
    public void visit(Player player) {
        if (player.getProtectedTicks() > 0) return;

        if (player.hasKey() && isHasChild()) {
            player.setChildrenCount(player.getChildrenCount() + 1);
            player.setHasKey(false);

            children--;
            if(children == 0) hasChild = false;

        } else if (!player.hasKey()) {
            player.setChildrenCount(player.getChildrenCount() - 1);
            children++;
            hasChild = true;
        }

        player.setProtectedTicks(25);
        notifyObservers(observers);
    }

    /**
     * provides information on whether a child is being held captive.
     *
     * @return true/false
     */
    public boolean isHasChild() {
        return hasChild;
    }

    /**
     * setter for hasChild attribute
     *
     * @param hasChild true/false
     */
    public void setHasChild(boolean hasChild) {
        this.hasChild = hasChild;
    }

    /**
     * replace unvisted with visited tiles eg. with a child being held captive.
     */
    @Override
    public void repaintAfterVisit() {
        if (hasChild) {
            this.tileset = TileCollection.getGingerbreadHouseVisitedTiles();
        } else {
            this.tileset = TileCollection.getGingerbreadHouseUnvisitedTiles();
        }
    }

    @Override
    public void setX(int x) {
        super.setX(x);
        GingerbreadHouse.PosX = x;
        System.out.println(GingerbreadHouse.PosX);
    }

    @Override
    public void setY(int y) {
        super.setY(y);
        GingerbreadHouse.PosY = y;
        System.out.println(GingerbreadHouse.PosY);
    }

    // public static void setInstance(GingerbreadHouse instance) {
    //    GingerbreadHouse.instance = instance;
    // }

    public int getChildren() {
        return children;
    }

    public void setChildren(int children) {
        this.children = children;
    }

}



