package org.hnu.netty.channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class CloseFutureClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();

        ChannelFuture channelFuture = new Bootstrap()
                .group(group)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new LoggingHandler(LogLevel.DEBUG));
                        nioSocketChannel.pipeline().addLast(new StringEncoder());
                    }
                })
                .connect("localhost", 8080);
        Channel channel = channelFuture.sync().channel();

        log.debug("{}", channel);
        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while(true) {
                String line = scanner.nextLine();
                if("q".equals(line)) {
                    channel.close(); //也是一个异步方法，没有立刻完成
                    break;
                }
                channel.writeAndFlush(line);
            }
        }, "input").start();

        ChannelFuture closedFuture = channel.closeFuture();
        //或者也可以采用sync阻塞
        /*log.debug("waiting close...");
        closeFuture.sync();
        log.debug("处理关闭之后的操作");*/

        closedFuture.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                log.debug("关闭之后的操作");
                group.shutdownGracefully();
            }
        });
    }
}
