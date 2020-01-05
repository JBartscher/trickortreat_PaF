package main.java.Network;

import main.java.AliceCooper;

public class CooperData extends EntityData {


    private boolean isPlaying;
    private boolean available = true;

    public CooperData(AliceCooper cooperData) {
        super(cooperData);

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
