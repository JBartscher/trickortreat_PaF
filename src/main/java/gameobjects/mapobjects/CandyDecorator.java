package main.java.gameobjects.mapobjects;

import main.java.Game;
import main.java.Sound;
import main.java.gameobjects.Player;

import java.util.Random;

public class CandyDecorator extends HouseDecorator {

    /**
     * the constructor of the concrete Decorator Object
     *
     * @param house House which will be decorated
     */
    public CandyDecorator(House house) {
        super(house);
    }

    /**
     * this method decorates the behavior of the visit of a house instance. It calls the objects visit method and adds
     * the play of the corresponding sound effect.
     *
     * @param player Player object which visits the House
     */
    @Override
    public void visit(Player player) {

        if (house instanceof Mansion || house instanceof TownHall || house instanceof GingerbreadHouse) {
            System.out.println("no Candy distributed!");
        }
        else if (house instanceof BigHouse && house.isUnvisited()) {
            System.out.println("BigHouse - 2x the Candy!");
            player.addCandy(calculateCandyAmount(player.childrenCount, 1.25));
            //house.isUnvisited = false;
            //notify observers

        } else if (house instanceof SmallHouse && house.isUnvisited()) {
            System.out.println("BigHouse - 1x the Candy!");
            player.addCandy(calculateCandyAmount(player.childrenCount, 1));
            //house.isUnvisited = false;
            //notify observers

        } else throw new IllegalAccessError("The Object is not of any known House type! It cannot be decorated/called by the CandyDecorator");

        super.visit(player);
    }

    /**
     * calculate the amount of candy a player gets when he visits a (regular) house.
     *
     * @param childrenCount number of children that the player controls
     * @param houseTypemultiplikator an factor that increases the amount of candy when visiting a bighouse instead of
     *                               a small one
     * @return an Integer that represents the amount of candy that the player gets
     */
    private int calculateCandyAmount(int childrenCount, double houseTypemultiplikator){
        // calculate the amount of candy the player gets
        int candies = 0;
        Random random = new Random();
        for (int i = 0; i < childrenCount; i++) {
            int zahl = random.nextInt(2);
            candies += (int) (house.district.getCandy_multiplikator() + zahl);
        }
        candies = (int) (candies * houseTypemultiplikator);
        System.out.println("CANDY:" + candies);
        return candies;
    }
}

