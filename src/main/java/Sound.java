package main.java;

import main.java.Configuration;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.util.Duration;

public class Sound {

    private final static Configuration<Object> config = new Configuration<Object>();

    private final static Media music = new Media(new File((String) config.getParam("musicFile")).toURI().toString());
    private final static MediaPlayer musicPlayer = new MediaPlayer(music);

    private final static Media ring = new Media(new File((String) config.getParam("ringFile")).toURI().toString());
    private final static MediaPlayer ringPlayer = new MediaPlayer(ring);

    private final static Media child = new Media(new File((String) config.getParam("childFile")).toURI().toString());
    private final static MediaPlayer childPlayer = new MediaPlayer(child);

    private final static Media cooper = new Media(new File((String) config.getParam("cooperFile")).toURI().toString());
    private final static MediaPlayer cooperPlayer = new MediaPlayer(cooper);

    private final static Media countdown = new Media(new File((String) config.getParam("countdownFile")).toURI().toString());
    private final static MediaPlayer countdownPlayer = new MediaPlayer(countdown);

    private final static Media gameover = new Media(new File((String) config.getParam("gameoverFile")).toURI().toString());
    private final static MediaPlayer gameoverPlayer = new MediaPlayer(gameover);

    private Sound() {
    }

    // music
    public static void playMusic() {

        try {
                stopMusic();
    
                // loop
                musicPlayer.setOnEndOfMedia(() -> {
                    musicPlayer.seek(Duration.ZERO);
                    musicPlayer.play();
                });
    
                musicPlayer.play();

                if ((Boolean) config.getParam("muted")) musicPlayer.setMute(true);

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    public static void playCountdown() {

        try {
                stopMusic();

                countdownPlayer.play();

                if ((Boolean) config.getParam("muted")) countdownPlayer.setMute(true);

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    public static void playCooper() {

        try {
                stopMusic();

                cooperPlayer.play();

                if ((Boolean) config.getParam("muted")) cooperPlayer.setMute(true);
                
        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    } 

    // effects
    public static void playRing() {

        try {
            ringPlayer.seek(Duration.ZERO);
            ringPlayer.play();

            if ((Boolean) config.getParam("muted")) ringPlayer.setMute(true);

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    public static void playChild() {

        try {
            childPlayer.seek(Duration.ZERO);
            childPlayer.play();

            if ((Boolean) config.getParam("muted")) childPlayer.setMute(true);

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    public static void playGameover() {

        try {
            stopMusic();

            gameoverPlayer.seek(Duration.ZERO);
            gameoverPlayer.play();

            if ((Boolean) config.getParam("muted")) gameoverPlayer.setMute(true);

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    // controls
    public static void muteSound() {

        try {
            if (!(Boolean) config.getParam("muted")) {

                musicPlayer.setMute(true);
                ringPlayer.setMute(true);
                countdownPlayer.setMute(true);
                cooperPlayer.setMute(true);
                childPlayer.setMute(true);
                gameoverPlayer.setMute(true);

                config.setParam("muted", true);

            } else {

                musicPlayer.setMute(false);
                ringPlayer.setMute(false);
                countdownPlayer.setMute(false);
                cooperPlayer.setMute(false);
                childPlayer.setMute(false);
                gameoverPlayer.setMute(false);

                config.setParam("muted", false);
            }

        } catch (NoClassDefFoundError ex) {
            ex.printStackTrace();
        }
    }

    public static void stopMusic() {

        musicPlayer.stop();
        countdownPlayer.stop();
        cooperPlayer.stop();
    }
}