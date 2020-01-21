package main.java.gameobjects.mapobjects;

import main.java.Singleton;
import main.java.Sound;
import main.java.gameobjects.Player;
import main.java.gameobjects.mapobjects.districts.District;
import main.java.map.TileCollection;

public class GingerbreadHouse extends House implements Singleton {

    /**
     * Singleton
     */
    static GingerbreadHouse instance = null;

    /**
     * boolean whether the house is holding a child
     */
    private boolean hasChild;

    /**
     * private constructor to ensure the Singleton is not instantiated in any other places.
     *
     * @param tileWidth  width - see BigHouse.BIG_HOUSE_WIDTH
     * @param tileHeight height - see BigHouse.BIG_HOUSE_HEIGHT
     */
    private GingerbreadHouse(int tileWidth, int tileHeight) {
        super(0, 0, tileWidth, tileHeight);
        tileset = TileCollection.getGingerbreadHouseUnvisitedTiles();
    }

    /**
     * unlike the other houses this object is designed as a singleton.
     * This is done to ensure that the witch can choose the house as starting point and spawn point
     * without the map generator having to know how or when the witch object is created.
     * <p>
     * It is crucial that the MapGenerator corrects the x,y and if applicable: the width and height of the object,
     * because they are given only DEFAULT values.
     *
     * @return an Instance of the GingerbreadHouse
     * @see GingerbreadHouse#setX(int) <-- correct value in MapGenerator
     * @see GingerbreadHouse#setY(int) <-- correct value in MapGenerator
     */
    public static GingerbreadHouse getInstance() {
        if (GingerbreadHouse.instance == null) {
            instance = new GingerbreadHouse(BigHouse.BIG_HOUSE_WIDTH, BigHouse.BIG_HOUSE_HEIGHT);
        }
        return GingerbreadHouse.instance;
    }

    /**
     * get unvisited tiles for the GingerbreadHouse
     *
     * @param district district (always normalDistrict)
     */
    @Override
    public void setDistrict(District district) {
        super.setDistrict(district);

        GingerbreadHouse.getInstance().tileset = district.getGingerbreadHouseUnvisitedTileset();
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
            Sound.playRing();
            player.setChildrenCount(player.getChildrenCount() + 1);
            player.setHasKey(false);
            hasChild = false;
        } else if (!player.hasKey()) {
            Sound.playRing();
            Sound.playChild();
            player.setChildrenCount(player.getChildrenCount() - 1);
            Sound.playChild();
            hasChild = true;
        }

        player.setProtectedTicks(50);
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
            GingerbreadHouse.getInstance().tileset = district.getGingerbreadHouseVisitedTileset();
        } else {
            GingerbreadHouse.getInstance().tileset = district.getGingerbreadHouseUnvisitedTileset();
        }
    }

}



