package main.java.Menu;

import java.io.Serializable;

public class HighScoreItem implements Serializable {

    private String name;
    private int score;

    public HighScoreItem(String name, int score){
        this.name = name;
        this.score = score;

    }


    public String toString(){
        return name + " - Punktzahl: " + score;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

}