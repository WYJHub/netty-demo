package org.hnu.netty.future;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class AsyncSuccess {
    public static void main(String[] args) {
        DefaultEventLoop defaultEventLoop = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(defaultEventLoop);

        //设置回调，异步接收结果
        promise.addListener(future -> {
            log.debug("{}", future.getNow());
        });

        defaultEventLoop.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("set success{}", 10);
            promise.setSuccess(10);
        });

        log.debug("start...");
    }
}
