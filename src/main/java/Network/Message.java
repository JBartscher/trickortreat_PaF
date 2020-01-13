package main.java.Network;

import java.io.*;

public class Message implements Serializable, Cloneable {

    public static <T> T deepCopy( T o ) throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream( baos ).writeObject( o );

        ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
        Object p = new ObjectInputStream( bais ).readObject();

        return (T) p;
    }

    private static final long serialVersionUID = 4678334060686032274L;
    private Type messageType;
    private GameState gameState;

    public enum Type {
        INIT, GAMESTATE, EVENT
    }


    public Message(Type type, GameState gameState) {
        this.messageType = type;
        this.gameState = gameState;
    }


    public GameState getGameState() { return gameState; }
    public Type getMessageType() { return messageType; }
}
