package main.java.net;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;

public class NetTest {

    public static void main(String[] args) {

        InetSocketAddress socketAddress = new InetSocketAddress(80);
        output(socketAddress);

        try {
            InetAddress address = InetAddress.getByName("www.hannover.de");
            socketAddress = new InetSocketAddress(address, 80);
            output(socketAddress);
        } catch (UnknownHostException e) {
            System.out.println(e.getMessage());
        }

        socketAddress = new InetSocketAddress("www.google.de", 80);
        output(socketAddress);

        socketAddress = new InetSocketAddress("localhost", 80);
        output(socketAddress);


    }

    private static void output(InetSocketAddress socketAddress) {
        System.out.println(
                socketAddress.getAddress() + "\n" +
                socketAddress.getHostName() + "\n" +
                socketAddress.getPort() + "\n");
    }

}
