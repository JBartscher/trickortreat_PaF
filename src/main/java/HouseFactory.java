package main.java;

import main.java.gameobjects.mapobjects.BigHouse;
import main.java.gameobjects.mapobjects.GingerbreadHouse;
import main.java.gameobjects.mapobjects.House;
import main.java.gameobjects.mapobjects.SmallHouse;

public class HouseFactory {


    public static House createNewInstance(String type){

        if(type.equalsIgnoreCase("small")) {
            return new SmallHouse(0, 0, 2, 2);
        } else if(type.equalsIgnoreCase("big")) {
            return new BigHouse(0, 0, 2, 3);
        } else if (type.equalsIgnoreCase("witch")) {

            System.out.println("HEXENHAUS erstellt!");
            return new GingerbreadHouse(0, 0, 2, 3);
        }

        return null;



    }
}
