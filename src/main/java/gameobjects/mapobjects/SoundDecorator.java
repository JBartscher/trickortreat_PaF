package main.java.gameobjects.mapobjects;

import main.java.Game;
import main.java.sounds.Sound;
import main.java.gameobjects.Player;

/**
 * concrete decorator
 */
public class SoundDecorator extends HouseDecorator {
    /**
     * the constructor of the concrete Decorator Object
     *
     * @param house House which will be decorated
     */
    public SoundDecorator(House house) {
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

        super.visit(player);
        //if(player.getProtectedTicks() > 0) return;


        if (house instanceof Mansion) {
            System.out.println("play Alice Cooper Music!");
            /**
             * if the player is IN the mansion the alice cooper music starts if hes outside the game music or the
             * "dramatic" music starts again.
             */
            if (house.isUnvisited()) {
                /**
                 * this is necessary to prevent ugly sound loops
                 */
                if (player.getProtectedTicks() > 0) return;
                if (Game.DRAMATIC) {
                    Sound.playCountdown();
                } else {
                    Sound.playMusic();
                }
            }
            Sound.playCooper();
        } else if (house instanceof GingerbreadHouse) {

            if(((GingerbreadHouse) house).isHasChild() && !player.hasKey()) {
                System.out.println("play kids scream Sound!");
                Sound.playChild();
            } else {
                Sound.playFree();
            }
            Sound.playRing();


        } else if (house instanceof TownHall) {
            System.out.println("Townhall - play no Sound!");
        } else {
            /**
             * small and big houses
             */
            System.out.println("play Doorbell Sound!");
            Sound.playRing();
        }

    }
}
