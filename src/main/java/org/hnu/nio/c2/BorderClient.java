package org.hnu.nio.c2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

/*
用于测试分段的客户端
 */
public class BorderClient {
    public static void main(String[] args) throws IOException {
        SocketChannel channel = SocketChannel.open();
//        channel.configureBlocking(false);
        channel.connect(new InetSocketAddress("127.0.0.1", 8080));
        channel.write(StandardCharsets.UTF_8.encode("Hello World!"));
        channel.write(StandardCharsets.UTF_8.encode("123456789\n"));
        channel.write(StandardCharsets.UTF_8.encode("Hello \nWorld!\n"));
    }
}
