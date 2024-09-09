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

//非阻塞的server，但是当前线程会由于非阻塞模式，没有新的连接时，该线程也会一直占用CPU
@Slf4j
public class NServer {
    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8080));
        serverSocketChannel.configureBlocking(false);
        ByteBuffer buffer = ByteBuffer.allocate(16);
        List<SocketChannel> socketChannels = new ArrayList<SocketChannel>();
        while (true) {
            SocketChannel socketChannel = serverSocketChannel.accept();//没有新的连接，返回null
            if (socketChannel != null) {
                log.debug("connected.. {}", socketChannel);
                socketChannel.configureBlocking(false);//影响其对应的read方法，修改read为非阻塞，没有读取到数据返回零
                socketChannels.add(socketChannel);
            }
            for(SocketChannel socketChannel2 : socketChannels) {
                //接收客户端发送的数据
                int read = socketChannel2.read(buffer);
                if (read > 0) {
                    buffer.flip();
                    debugAll(buffer);
                    buffer.clear();
                    log.debug("after read.. {}", socketChannel2);
                }
            }
        }
    }
}
