package main.java.net.server;

import main.java.net.model.Digest;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class DigestServer {

    private static final int BUFSIZE = 508;

    public static void main(String[] args) {
        int port = 50000;
        String remoteHost = "localhost";
        int remotePort = 40000;

        try (DatagramSocket socket = new DatagramSocket(port)) {
            socket.connect(InetAddress.getByName(remoteHost), remotePort);

            // send and receive packets
            DatagramPacket packetIn  = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);
            DatagramPacket packetOut = new DatagramPacket(new byte[BUFSIZE], BUFSIZE);

            System.out.println("Server is running and listening on port: " + port + " ...");

            //noinspection InfiniteLoopStatement
            while (true) {
                // receive packet
                socket.receive(packetIn);
                System.out.println("Received: " + packetIn.getLength() + " bytes");

                String json = new String(packetIn.getData(), 0, packetIn.getLength());

                Digest digest = deserialize(json);
                String hashValue = digest(digest.getText());
                digest.setHashValue(hashValue);

                byte[] data = serialize(digest).getBytes();

                // store data and data length in response
                packetOut.setData(data);
                packetOut.setLength(data.length);

                // set destination address and port for response
                packetOut.setSocketAddress(packetIn.getSocketAddress());

                // send response
                socket.send(packetOut);
            }
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
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

    private static String digest(String data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(data.getBytes());
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }
}
