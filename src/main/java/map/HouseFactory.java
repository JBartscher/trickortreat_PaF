package main.java.map;

import main.java.gameobjects.mapobjects.BigHouse;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.SmallHouse;
import main.java.pattern.Factory;

/**
 * this class implements the factory pattern and allows a generic way to create new house objects
 */
public class HouseFactory implements Factory<House> {
    /**
     * factory method to create instances of houses
     *
     * @param type String type of house
     * @return a instance of that type
     */
    public House createNewInstance(String type) {

        if (type.equalsIgnoreCase("small")) {
            return new SmallHouse(0, 0, SmallHouse.SMALL_HOUSE_WIDTH, SmallHouse.SMALL_HOUSE_HEIGHT);
        } else if (type.equalsIgnoreCase("big")) {
            return new BigHouse(0, 0, BigHouse.BIG_HOUSE_WIDTH, BigHouse.BIG_HOUSE_HEIGHT);
        } else if (type.equalsIgnoreCase("witch")) {
            return new GingerbreadHouse(0, 0, BigHouse.BIG_HOUSE_WIDTH, BigHouse.BIG_HOUSE_HEIGHT);
        }

        return null; //type not matching
    }
}
