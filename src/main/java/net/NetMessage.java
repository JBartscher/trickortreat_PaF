package main.java.net;

import java.time.LocalDateTime;

public class NetMessage {

    private String user;
    private LocalDateTime timestamp;
    private String message;

    public NetMessage() {}

    public NetMessage(String user, LocalDateTime timestamp, String message) {
        this.user = user;
        this.timestamp = timestamp;
        this.message = message;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "NetMessage { user='" + user + '\'' + ", " +
                "timestamp=" + timestamp + ", message='" + message + '\'' + '}';
    }
}
