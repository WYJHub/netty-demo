package org.hnu.nio.c2;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import static org.hnu.nio.c1.ByteBufferUtil.debugAll;

//实现通过分割符来解析消息的server
@Slf4j
public class BorSelectorServer {
    public static void main(String[] args) throws IOException {
        Selector selector = Selector.open();

        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.bind(new InetSocketAddress(8080));
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            /*
            select(time)阻塞直到事件发生或者超时
            selectNow()直接返回
             */
            selector.select();//阻塞直到绑定事件发生
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel client = server.accept();
                    client.configureBlocking(false);
                    ByteBuffer buffer = ByteBuffer.allocate(16);
                    SelectionKey sk = client.register(selector, SelectionKey.OP_READ, buffer);
                    log.info("新的连接");
                    log.debug("accept:{}", client);
                    log.debug("sk:{}", sk);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        int read = sc.read(buffer);
                        if(read == -1) {
                            key.cancel();
                        }
                        else {
                            split(buffer);
                            if(buffer.position() == buffer.limit()) {
                                ByteBuffer newBuffer = ByteBuffer.allocate(buffer.capacity() * 2);
                                buffer.flip();
                                newBuffer.put(buffer);
                                key.attach(newBuffer);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        key.cancel();
                    }
                }
            }
        }

    }

    private static void split(ByteBuffer source) {
        source.flip();
        int allLength = source.limit();
        for (int i = 0; i < allLength; i++) {
            if (source.get(i) == '\n') {
                System.out.println(i);
                ByteBuffer target = ByteBuffer.allocate(i + 1 - source.position());
                // 0 ~ limit
                // source当前为读模式，即position为0，limit为当前位置+1
                source.limit(i + 1); //通过修改limit，然后拷贝buffer的方法，比spilt中的普通get()方法然后粘贴过去更快。
                target.put(source); // 从source 读，向 target 写
                debugAll(target);
                source.limit(allLength);//将limit修改回来
            }
        }
        source.compact();//将source切换为写模式
    }
}
