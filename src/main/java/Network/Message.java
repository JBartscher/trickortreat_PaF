package main.java.Network;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private Type messageType;
    private GameState gameState;

    public enum Type {
        INIT, GAMESTATE, EVENT, GAMEOVER
    }


    public Message(Type type, GameState gameState) {
        this.messageType = type;
        this.gameState = gameState;
    }


    public GameState getGameState() { return gameState; }
    public Type getMessageType() { return messageType; }
}
