package main.java.Network;

import main.java.AliceCooper;

public class CooperData extends EntityData {


    private boolean isPlaying;

    public CooperData(AliceCooper cooperData) {
        super(cooperData);

        this.isPlaying = cooperData.isPlaying();
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

}