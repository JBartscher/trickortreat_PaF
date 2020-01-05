package main.java;

import javafx.scene.image.Image;
import main.java.Network.CooperData;
import main.java.gameobjects.Player;

public class AliceCooper extends Entity {


    public AliceCooper() {

        this.sprite = new Image(AliceCooper.class.getResourceAsStream("alice_cooper.png"));
    }


    private boolean isPlaying;
    private boolean available = true;

    public void playSong(Player player) {

        if(!isPlaying && available){
            isPlaying = true;
            available = false;
            player.setProtected(true);
            Sound.playMusic();

        }
    }

    public void setGameStateData(CooperData cooperData) {
        super.setGameStateData(cooperData);

        this.isPlaying = cooperData.isPlaying();
        this.available = cooperData.isAvailable();

    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }


}
