package org.hnu.netty.example;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.nio.charset.Charset;
import java.util.Scanner;

public class Example {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();

        Channel channel = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringEncoder());


                        nioSocketChannel.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buffer = (ByteBuf) msg;
                                System.out.println(buffer.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                }).connect("localhost", 8080).syncUninterruptibly().channel();

        channel.closeFuture().addListener(future -> {
            group.shutdownGracefully();
        });

        new Thread(() -> {
            Scanner sc = new Scanner(System.in);
            while(true) {
                String line = sc.nextLine();
                if("q".equals(line)) {
                    channel.close();
                    break;
                }
                channel.writeAndFlush(line);
            }
        }).start();
    }
}
