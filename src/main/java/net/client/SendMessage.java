package main.java.net.client;

import main.java.net.NetMessage;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.*;
import java.time.LocalDateTime;

public class SendMessage {

    private static final int BUFSIZE = 508;

    public static void main(String[] args) throws Exception {
        String host = "localhost";
        int port = 50000;

        String message = createMessage();

        try (DatagramSocket socket = new DatagramSocket()) {
            InetAddress address = InetAddress.getByName(host);
            DatagramPacket packet = new DatagramPacket(new byte[BUFSIZE], BUFSIZE, address, port);
            byte[] data = message.getBytes();
            packet.setData(data);
            packet.setLength(data.length);
            socket.send(packet);
        }
    }

    private static String createMessage() {
        NetMessage msg = new NetMessage("Player1", LocalDateTime.now(), "HELLO");
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(msg);
    }
}
