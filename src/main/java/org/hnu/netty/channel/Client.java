package org.hnu.netty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

//拆分客户端代码，查看信息
public class Client {

    public static void main(String[] args) throws InterruptedException {
        ChannelFuture connect = new Bootstrap()
                .group(new NioEventLoopGroup())
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("127.0.0.1", 8080);//connect方法返回的是一个ChannelFuture对象，利用channel方法来获取Channel对象
        //connect方法是一个异步方法，返回的Future对象不能立刻获得正确的channel对象

        System.out.println(connect.channel());
        connect.sync(); //等待同步连接完成
        System.out.println(connect.channel());
        /*
            [id: 0x4cceb3b8] 第一次打印连接未建立
            [id: 0x4cceb3b8, L:/127.0.0.1:14290 - R:/127.0.0.1:8080]
         */
    }
}
