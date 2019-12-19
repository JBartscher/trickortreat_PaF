package main.java.net.client;

import main.java.net.model.Digest;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.*;
import java.sql.CallableStatement;

public class DigestClient {

    private static final int BUFSIZE = 508;
    private static final int TIMEOUT = 10000;

    public static void main(String[] args) {
        int localPort = 40000;
        String host = "localhost";
        int port = 50000;
        String text = "Das ist ein Test";

        try (DatagramSocket socket = new DatagramSocket(localPort)) {
            socket.setSoTimeout(TIMEOUT);

            byte[] data = serialize(new Digest(text)).getBytes();

            // send packet to server
            InetAddress addr = InetAddress.getByName(host);
            DatagramPacket packetOut = new DatagramPacket(data, data.length, addr, port);
            socket.send(packetOut);

            // receive response
            DatagramPacket packetIn = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
            socket.receive(packetIn);

            String json = new String(packetIn.getData(), 0, packetIn.getLength());
            Digest digest = deserialize(json);
            System.out.println(digest.getText());
            System.out.println(digest.getHashValue());
        } catch (SocketTimeoutException e) {
            System.err.println("Timeout: " + e.getMessage());
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    private static Digest deserialize(String data) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.fromJson(data, Digest.class);
    }

    private static String serialize(Digest digest) {
        Jsonb jsonb = JsonbBuilder.create();
        return jsonb.toJson(digest);
    }
}
