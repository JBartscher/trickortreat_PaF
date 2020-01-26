package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;

/**
 * Interface which informs a implementing class that the class instance is accessible by playerEntities.
 * Every accessible house must implement this interface.
 * @see TownHall
 * @see Mansion
 */
public interface Accessible {

    /**
     * set the inside mode of a instance (outside/inside).
     *
     * @param player playerEntity
     */
    void setInsideMode(Player player);

}
