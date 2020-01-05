package main.java;

import java.io.Serializable;

public class Message implements Serializable {


    private Type messageType;
    private Object o;

    public enum Type {
        INIT, GAMESTATE, EVENT
    }


    public Message(Type type, Object o) {
        this.messageType = type;
        this.o = o;
    }


    public Object getObject() { return o; }
    public Type getMessageType() { return messageType; }
}
