package main.java.sounds;

import main.java.configuration.Configuration;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

/** 
 * helper class for music and sound effects
 */
public class Sound {

    private final static Configuration<Object> config = new Configuration<Object>();

    /**
     * read filepathes from config parameters, sources:
     * countdown.mp3
     * https://freesoundeffects.com/free-track/witch-466442/
     * https://freemusicarchive.org/music/Haunted_Corpse/Dirges_for_the_Undead/Haunted_Corpse_-_Dirges_for_the_Undead_-_12_A_Ghost_in_the_Fog_1746
     * child.wav
     * https://freesound.org/people/estefaniabonnin/sounds/219864/
     * child2.wav
     * https://freesound.org/people/estefaniabonnin/sounds/219867/
     * poison.mp3
     * https://www.guitarbackingtrack.com/play/alice_cooper/poison_(2).htm
     * menu.wav
     * https://freesound.org/people/GameAudio/sounds/220183/
     * menu2.wav
     * https://freesound.org/people/GameAudio/sounds/220198/
     * music.wav
     * https://www.looperman.com/loops/detail/185175/halloween-type-melody-140bpm-rap-synth-loop
     * music2.wav
     * https://www.looperman.com/loops/detail/169805/dread-dark-kalimba-scope-synth-loop-165bpm-trap-bells-loop
     * gameover.mp3
     * http://www.orangefreesounds.com/you-lose-game-over/
     * ring.mp3
     * http://soundbible.com/1466-Doorbell.html
     * ring2.mp3
     * http://soundbible.com/165-Door-Bell.html
     * ring3.mp3
     * http://soundbible.com/1462-Two-Tone-Doorbell.html
     * ring4.wav
     * https://freesound.org/people/jwheeler91/sounds/442280/
     * ring5.wav
     * https://freesound.org/people/baujahr66/sounds/157250/
     */

    private final static MediaPlayer music = new MediaPlayer(new Media(new File("src/main/java/sounds/music.wav").toURI().toString()));
    private final static MediaPlayer ring1 = new MediaPlayer(new Media(new File("src/main/java/sounds/ring.mp3").toURI().toString()));
    private final static MediaPlayer ring2 = new MediaPlayer(new Media(new File("src/main/java/sounds/ring2.mp3").toURI().toString()));
    private final static MediaPlayer ring3 = new MediaPlayer(new Media(new File("src/main/java/sounds/ring3.mp3").toURI().toString()));
    private final static MediaPlayer ring4 = new MediaPlayer(new Media(new File("src/main/java/sounds/ring4.wav").toURI().toString()));
    private final static MediaPlayer ring5 = new MediaPlayer(new Media(new File("src/main/java/sounds/ring5.wav").toURI().toString()));
    private final static MediaPlayer child1 = new MediaPlayer(new Media(new File("src/main/java/sounds/child.wav").toURI().toString()));
    private final static MediaPlayer child2 = new MediaPlayer(new Media(new File("src/main/java/sounds/child2.wav").toURI().toString()));
    private final static MediaPlayer cooper = new MediaPlayer(new Media(new File("src/main/java/sounds/poison.mp3").toURI().toString()));
    private final static MediaPlayer countdown = new MediaPlayer(new Media(new File("src/main/java/sounds/countdown.mp3").toURI().toString()));
    private final static MediaPlayer gameover = new MediaPlayer(new Media(new File("src/main/java/sounds/gameover.mp3").toURI().toString()));
    private final static MediaPlayer menu = new MediaPlayer(new Media(new File("src/main/java/sounds/menu.wav").toURI().toString()));

    private final static Random random = new Random();
    private final static List<MediaPlayer> ringList = Arrays.asList(ring1,ring2,ring3,ring4,ring5);
    private final static List<MediaPlayer> childList = Arrays.asList(child1,child2);

    /**
     * private constructor to prevent initiation
     */
    private Sound() {
    }

    /**
     * choose random sound file
     * @param <T>
     * @param list
     * @return
     */
    private static <T> T getRandomItem(List<T> list) {

        if(list.isEmpty()) throw new IllegalArgumentException("Die Liste darf nicht leer sein!");

        T item = list.get(random.nextInt(list.size()));

        return item;
    }

    /**
     * background music
     */
    public static void playMusic() {

        try {
            // stop all music
            stopMusic();

            // play again on end of media (loop)
            music.setOnEndOfMedia(() -> {
                music.seek(Duration.ZERO);
                music.play();
            });

            // play background music
            music.play();

            // mute background music if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                music.setMute(true);
            } else {
                music.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * coundown for last 30 seconds
     */
    public static void playCountdown() {

        try {
            // stop all music
            stopMusic();

            // play countdown music
            countdown.play();

            // mute coundown music if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                countdown.setMute(true);
            } else {
                countdown.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * poison from cooper in mansion
     */
    public static void playCooper() {

        try {
            // stop all music
            stopMusic();

            // play cooper music
            cooper.play();

            // mute cooper music if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                cooper.setMute(true);
            } else {
                cooper.setMute(false);
            }     

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    } 

    /** 
     * door bell
     */ 
    public static void playRing() {

        try {
            // choose random door bell
            MediaPlayer ring = getRandomItem(ringList);

            // play door bell
            ring.seek(Duration.ZERO);
            ring.play();

            // mute door bell sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                ring.setMute(true);
            } else {
                ring.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * child catched by witch
     */
    public static void playChild() {

        try {
            // choose random child sound
            MediaPlayer child = getRandomItem(childList);

            // play child sound
            child.seek(Duration.ZERO);
            child.play();

            // mute child sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                child.setMute(true);
            } else {
                child.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /** 
     * gameover sound
     */
    public static void playGameover() {

        try {
            // stop all music
            stopMusic();

            // play gameover sound
            gameover.seek(Duration.ZERO);
            gameover.play();

            // mute game over sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                gameover.setMute(true);
            } else {
                gameover.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * menu item clicks
     */
    public static void playMenu() {

        try {
            // stop all music
            stopMusic();

            // play menu sound
            menu.seek(Duration.ZERO);
            menu.play();

            // mute menu sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                menu.setMute(true);
            } else {
                menu.setMute(false);
            }
            
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * unmute all sounds
     */
    public static void unmuteSound() {
        try {

                music.setMute(false);
                countdown.setMute(false);
                cooper.setMute(false);
                gameover.setMute(false);

                config.setParam("muted", false);

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }

    }
    
    /**
     * mute/unmute toggle
     */
    public static void muteSound() {

        try {
            if (!(Boolean) config.getParam("muted")) {

                music.setMute(true);
                countdown.setMute(true);
                cooper.setMute(true);
                gameover.setMute(true);
                ring1.setMute(true);
                ring2.setMute(true);
                ring3.setMute(true);
                ring4.setMute(true);
                ring5.setMute(true);



                // set muted to true in config
                config.setParam("muted", true);

            } else {

                music.setMute(false);
                countdown.setMute(false);
                cooper.setMute(false);
                gameover.setMute(false);
                ring1.setMute(false);
                ring2.setMute(false);
                ring3.setMute(false);
                ring4.setMute(false);
                ring5.setMute(false);

                // set muted to false in config
                config.setParam("muted", false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * stops only music
     */
    public static void stopMusic() {

        music.stop();
        countdown.stop();
        cooper.stop();
    }
}