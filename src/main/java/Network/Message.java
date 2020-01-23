package main.java.Network;

import java.io.*;

/**
 * this class represents the data that get transmitted between client and server
 * each message object has a gamestate and a type to determine if events occurred
 * also the interfaces Serializable and Cloneable are implemented to ensure deep copies when transmitting messages between the players
 */
public class Message implements Serializable, Cloneable {
    private static final long serialVersionUID = 4678334060686032274L;

    /**
     * create a deep copy of an message object
     */
    public static <T> T deepCopy( T o ) throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        new ObjectOutputStream( baos ).writeObject( o );

        ByteArrayInputStream bais = new ByteArrayInputStream( baos.toByteArray() );
        Object p = new ObjectInputStream( bais ).readObject();

        return (T) p;
    }

    private Type messageType;
    private GameState gameState;

    /**
     * each message has a type to establish the right reaction when receiving messages
     */
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
