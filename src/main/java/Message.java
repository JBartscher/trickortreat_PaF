package main.java;

import java.io.Serializable;

public class Message implements Serializable {


    Type messageType;
    private Object o;

    public enum Type {
        INIT, GAMESTATE
    }


    public Message(Type type, Object o) {
        this.messageType = type;
        this.o = o;
    }


    public Object getObject() { return o; }
}
