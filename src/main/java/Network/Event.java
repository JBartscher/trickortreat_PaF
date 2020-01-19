package main.java.Network;

import java.io.*;

public class Event implements Serializable, Cloneable {

    private static final long serialVersionUID = 744265289056445070L;
    private Object object;
    private EventType type;

    public enum EventType implements Serializable {
       VISITED, COLLISION, TOWNHALL, KIDNAPPING, PAUSED, UNPAUSED, REPLAY
    }

    public Event(Object o, EventType type) {
        this.object = o;
        this.type = type;
    }

    public EventType getType() {
        return type;
    }

    public void setType(EventType type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String toString() {
        return " EVENT-TYP: " + type + " ";
    }
}
