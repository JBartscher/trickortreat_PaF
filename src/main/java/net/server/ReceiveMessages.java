package main.java.net.server;

import main.java.net.NetMessage;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveMessages {

    private static final int BUFSIZE = 508;

    public static void main(String[] args) {
        int port = 50000;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            DatagramPacket packet = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

            //noinspection InfiniteLoopStatement
            while (true) {
                socket.receive(packet);
                String data = new String(packet.getData(), 0, packet.getLength());
                NetMessage message = deserialize(data);
                System.out.println(data);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static NetMessage deserialize(String data) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.fromJson(data, NetMessage.class);
    }
}
