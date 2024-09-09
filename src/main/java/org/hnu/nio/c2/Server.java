package org.hnu.nio.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;

import static org.hnu.nio.c1.ByteBufferUtil.debugAll;

@Slf4j
public class Server {
    public static void main(String[] args) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(16);
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false); //非阻塞模式
        List<SocketChannel> socketChannels = new ArrayList<SocketChannel>();
        while (true) {
            log.debug("connecting");
            SocketChannel socketChannel = serverSocketChannel.accept();//阻塞方法，线程停止运行，直到有新线程连接
            log.debug("connected ... {}", socketChannel );
            socketChannels.add(socketChannel);
            for(SocketChannel socketChannel2 : socketChannels){
                log.debug("before read ...{}", socketChannel2);
                socketChannel.read(buffer);//阻塞方法，线程停止运行直到读取到数据
                buffer.flip();
                debugAll(buffer);
                buffer.clear();
                log.debug("after read ...{}", socketChannel2);
            }
        }
    }
}
