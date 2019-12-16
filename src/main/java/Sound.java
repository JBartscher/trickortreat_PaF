package main.java;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Sound {

    public final static Media music = new Media(new File("src/main/java/sounds/looperman-l-2384498-0185175-halloween-type-melody.wav").toURI().toString());
    public final static MediaPlayer musicPlayer = new MediaPlayer(music);

    public final static Media ring = new Media(new File("src/main/java/sounds/Doorbell-SoundBible.com-516741062.mp3").toURI().toString());
    public final static MediaPlayer ringPlayer = new MediaPlayer(ring);

    private Sound() {
    }

    public static void playMusic() {
        musicPlayer.setOnEndOfMedia(() -> {
            musicPlayer.seek(Duration.ZERO);
            musicPlayer.play();
        });
        musicPlayer.play();
    }

    public static void playRing() {
        ringPlayer.seek(Duration.ZERO);
        ringPlayer.play();
    }
}