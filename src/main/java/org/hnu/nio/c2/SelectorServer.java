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

//通过Selector来监听多个channel的事件，事件发生时才让线程去处理。避免在非阻塞模式下所作无用功
@Slf4j
public class SelectorServer {
    public static void main(String[] args) throws IOException {
        //1.创建selector,管理多个channel
        Selector selector = Selector.open();
        ServerSocketChannel ssc = ServerSocketChannel.open(); //监听的channel必须工作在非阻塞模式
        ssc.configureBlocking(false);
        ssc.bind(new InetSocketAddress(8080));

        //2.建立selector和channel之间的联系
        SelectionKey selectionKey = ssc.register(selector, SelectionKey.OP_ACCEPT);//指定监听ssc的连接事件
        log.debug("register selector:{}" , selectionKey);
/*
            除此之外事件还有
             SelectionKey.OP_CONNECT 客户端连接成功时触发
             SelectionKey.OP_ACCEPT 服务器端成功接收连接时触发
             SelectionKey.OP_READ 数据可读入时触发，有因为接收能力弱，数据暂不能读入的情况
             SelectionKey.OP_WRITE 数据可写出时触发，有因为发送能力弱，数据暂不能写出的情况
         */
        while (true) {
           //3.select方法，没有事件发生，线程阻塞，有事件，线程才会恢复运行
            selector.select();
            //4.处理事件，selectedKeys中包含了内部发生的所有事件
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while(iterator.hasNext()) {
                SelectionKey key = iterator.next();
                //selector在事件发生后，会将相应的key放入selectedKeys中
                //如果处理key时没有删除，那么下次访问到该key，想通过该key获取对应的channel时，
                //如果该连接已经断开了，即没有实际存在的sc了，就会出现空指针异常
                iterator.remove();
                log.debug("key: {}", key);
                //5.判断事件类型
                if (key.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = channel.accept();//处理事件
                    socketChannel.configureBlocking(false);
                    SelectionKey scKey = socketChannel.register(selector, SelectionKey.OP_READ);
                    //或者通过取消事件 key.cancel()
                    //NIO底层采用的是水平触发而不是边缘触发，所以如果读取到事件发生，要么取消事件、要么执行完事件，否则会一直处理该事件
                    log.debug("accept: {}", socketChannel);
                    log.debug("key: {}", scKey);
                } else if (key.isReadable()) {
                    try {
                        SocketChannel sc = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(16);
                        int read = sc.read(buffer);
                        if(read >= 0) {
                            buffer.flip();
                            buffer.clear();
                        } else { //key == -1时，不需要再执行read事件了。对应channel正常退出
                            key.cancel();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        key.cancel(); //cancel的作用，取消注册在selector上的channel，并且将对应的keys集合中删除key，后续不再监听事件
                    }
                }

            }
        }
    }
}
