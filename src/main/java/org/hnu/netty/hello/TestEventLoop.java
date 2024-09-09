package org.hnu.netty.hello;

import io.netty.channel.DefaultEventLoop;
import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.util.NettyRuntime;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestEventLoop {
    public static void main(String[] args) {
        //private static final int DEFAULT_EVENT_LOOP_THREADS = Math.max
        //(1, SystemPropertyUtil.getInt("io.netty.eventLoopThreads", NettyRuntime.availableProcessors() * 2));
        //创建指定线程数，或者默认线程
        NioEventLoopGroup group = new NioEventLoopGroup(2);//io任务，普通任务、定时任务
//        DefaultEventLoopGroup group2 = new DefaultEventLoopGroup(2);//执行普通任务、定时任务

//        System.out.println(NettyRuntime.availableProcessors()); 可用核心数

        System.out.println(group.next());
        System.out.println(group.next());
        //循环出现
        System.out.println(group.next());
        System.out.println(group.next());


        //普通任务
/*        group.next().submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("ok");
        });//异步执行
        log.debug("main");*/

        group.execute(() -> {
            log.debug("normal task..");
        });
        log.info("main");
        //执行定时任务
/*        group.next().scheduleAtFixedRate(() -> {
            log.debug("ok");
        }, 0, 1, TimeUnit.SECONDS);//初始延时值 + 时间间隔 + 时间单位
        log.debug("main");*/
    }

}
