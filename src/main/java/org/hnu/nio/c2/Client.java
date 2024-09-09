package org.hnu.nio.c2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.channels.SocketChannel;

public class Client {
    public static void main(String[] args) throws IOException {
        SocketChannel open = SocketChannel.open();
        open.connect(new InetSocketAddress("127.0.0.1", 8080));
        SocketAddress localAddress = open.getLocalAddress();
        System.out.println("waiting for client to connect");
//        open.write(StandardCharsets.UTF_8.encode("hello"));
    }
}
