package main.java.gameobjects.mapobjects;

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
        System.out.println(house);
        player.addCandy(calculateCandyAmount(player.childrenCount));
        super.visit(player);
    }

    /**
     * calculate the amount of candy a player gets when he visits a (regular) house.
     *
     * @param childrenCount number of children that the player controls
     * @return an Integer that represents the amount of candy that the player gets
     */
    private int calculateCandyAmount(int childrenCount) {
        // calculate the amount of candy the player gets
        int candies = 0;
        Random random = new Random();
        for (int i = 0; i < childrenCount; i++) {
            int zahl = random.nextInt(2);
            candies += (int) (((HouseDecorator)this.getDecoratedHouse()).getDecoratedHouse().district.getCandyMultiplikator() + zahl);
        }
        System.out.println("CANDY:" + candies);
        return candies;
    }
}

