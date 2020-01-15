package main.java;

import main.java.gameobjects.mapobjects.BigHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.SmallHouse;

public class HouseFactory {


    public static House createNewInstance(String type){

        if(type.equalsIgnoreCase("small")) {
            return new SmallHouse(0, 0, 2, 2);
        } else if(type.equalsIgnoreCase("big")) {
            return new BigHouse(0, 0, 2, 3);
        }

        return null;



    }
}
