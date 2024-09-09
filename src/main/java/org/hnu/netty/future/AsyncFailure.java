package org.hnu.netty.future;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AsyncFailure {
    public static void main(String[] args) {
        DefaultEventLoop defaultEventLoop = new DefaultEventLoop();
        DefaultPromise<Integer> promise = new DefaultPromise<>(defaultEventLoop);

        promise.addListener(future -> {
            log.debug("result: {}", (promise.isSuccess() ? promise.getNow() : promise.cause()).toString());
        });

        defaultEventLoop.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            RuntimeException e = new RuntimeException("error...");
            log.debug("set failure, {}", e.toString());
            promise.setFailure(e);
        });

        log.debug("start...");
    }
}
