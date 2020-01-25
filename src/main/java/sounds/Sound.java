package main.java.sounds;

import main.java.configuration.Configuration;

import java.io.File;

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

    private final static Media music = new Media(new File((String) config.getParam("musicFile")).toURI().toString());
    private final static Media ring = new Media(new File((String) config.getParam("ringFile")).toURI().toString());
    private final static Media child = new Media(new File((String) config.getParam("childFile")).toURI().toString());
    private final static Media cooper = new Media(new File((String) config.getParam("cooperFile")).toURI().toString());
    private final static Media countdown = new Media(new File((String) config.getParam("countdownFile")).toURI().toString());
    private final static Media gameover = new Media(new File((String) config.getParam("gameoverFile")).toURI().toString());
    private final static Media menu = new Media(new File((String) config.getParam("menuFile")).toURI().toString());

    private final static MediaPlayer musicPlayer = new MediaPlayer(music);
    private final static MediaPlayer ringPlayer = new MediaPlayer(ring);
    private final static MediaPlayer childPlayer = new MediaPlayer(child);
    private final static MediaPlayer cooperPlayer = new MediaPlayer(cooper);
    private final static MediaPlayer countdownPlayer = new MediaPlayer(countdown);
    private final static MediaPlayer gameoverPlayer = new MediaPlayer(gameover);
    private final static MediaPlayer menuPlayer = new MediaPlayer(menu);

    // private constructor to prevent initiation
    private Sound() {
    }

    /**
     * music
     */

    // background music
    public static void playMusic() {

        try {
            // stop all music
            stopMusic();

            // play again on end of media (loop)
            musicPlayer.setOnEndOfMedia(() -> {
                musicPlayer.seek(Duration.ZERO);
                musicPlayer.play();
            });

            // play background music
            musicPlayer.play();

            // mute background music if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                musicPlayer.setMute(true);
            } else {
                musicPlayer.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    // coundown for last 30 seconds
    public static void playCountdown() {

        try {
            // stop all music
            stopMusic();

            // play countdown music
            countdownPlayer.play();

            // mute coundown music if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                countdownPlayer.setMute(true);
            } else {
                countdownPlayer.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    // poison from cooper in mansion
    public static void playCooper() {

        try {
            // stop all music
            stopMusic();

            // play cooper music
            cooperPlayer.play();

            // mute cooper music if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                cooperPlayer.setMute(true);
            } else {
                cooperPlayer.setMute(false);
            }     

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    } 

    /** 
     * effects
     */

    // door bell
    public static void playRing() {

        try {
            // play door bell
            ringPlayer.seek(Duration.ZERO);
            ringPlayer.play();

            // mute door bell sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                ringPlayer.setMute(true);
            } else {
                ringPlayer.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    // child catched by witch
    public static void playChild() {

        try {
            // play child sound
            childPlayer.seek(Duration.ZERO);
            childPlayer.play();

            // mute child sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                childPlayer.setMute(true);
            } else {
                childPlayer.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    // gameover sound
    public static void playGameover() {

        try {
            // stop all music
            stopMusic();

            // play gameover sound
            gameoverPlayer.seek(Duration.ZERO);
            gameoverPlayer.play();

            // mute game over sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                gameoverPlayer.setMute(true);
            } else {
                gameoverPlayer.setMute(false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    // menu item clicks
    public static void playMenu() {

        try {
            // stop all music
            stopMusic();

            // play menu sound
            menuPlayer.seek(Duration.ZERO);
            menuPlayer.play();

            // mute menu sound if muted is true in config
            if ((Boolean) config.getParam("muted")) {
                menuPlayer.setMute(true);
            } else {
                menuPlayer.setMute(false);
            }
            
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    /**
     * controls
     */


    public static void unmuteSound() {
        try {

                musicPlayer.setMute(false);
                ringPlayer.setMute(false);
                countdownPlayer.setMute(false);
                cooperPlayer.setMute(false);
                childPlayer.setMute(false);
                gameoverPlayer.setMute(false);

                config.setParam("muted", false);

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }

    }
    
    // mutes all sounds
    public static void muteSound() {

        try {
            if (!(Boolean) config.getParam("muted")) {

                musicPlayer.setMute(true);
                ringPlayer.setMute(true);
                countdownPlayer.setMute(true);
                cooperPlayer.setMute(true);
                childPlayer.setMute(true);
                gameoverPlayer.setMute(true);

                // set muted to true in config
                config.setParam("muted", true);

            } else {

                musicPlayer.setMute(false);
                ringPlayer.setMute(false);
                countdownPlayer.setMute(false);
                cooperPlayer.setMute(false);
                childPlayer.setMute(false);
                gameoverPlayer.setMute(false);

                // set muted to false in config
                config.setParam("muted", false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    // stops all music
    public static void stopMusic() {

        musicPlayer.stop();
        countdownPlayer.stop();
        cooperPlayer.stop();
    }
}