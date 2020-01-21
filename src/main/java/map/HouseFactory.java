package main.java.map;

import main.java.gameobjects.mapobjects.BigHouse;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.SmallHouse;

public class HouseFactory {
    /**
     * factory method to create instances of houses
     *
     * @param type String type of house
     * @return a instance of that type
     */
    public static House createNewInstance(String type) {

        if (type.equalsIgnoreCase("small")) {
            return new SmallHouse(0, 0, SmallHouse.SMALL_HOUSE_WIDTH, SmallHouse.SMALL_HOUSE_HEIGHT);
        } else if (type.equalsIgnoreCase("big")) {
            return new BigHouse(0, 0, BigHouse.BIG_HOUSE_WIDTH, BigHouse.BIG_HOUSE_HEIGHT);
        } else if (type.equalsIgnoreCase("witch")) {
            /**
             * singleton
             */
            return GingerbreadHouse.getInstance();
        }

        return null; //type not matching
    }
}
