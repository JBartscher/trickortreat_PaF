package main.java.gameobjects.mapobjects;

import main.java.gameobjects.Player;

public class CandyDecorator extends HouseDecorator {

    public CandyDecorator(House house) {
        super(house);
    }


    @Override
    public void visit(Player player) {
        super.visit(player);
        System.out.println(" Candies einsammlen");

    }
}

