package org.hnu.netty.future;

import io.netty.channel.DefaultEventLoop;
import io.netty.util.concurrent.DefaultPromise;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;

@Slf4j
public class SyncFailure {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        DefaultEventLoop defaultEventLoop = new DefaultEventLoop();
        
        DefaultPromise<Integer> promise = new DefaultPromise<Integer>(defaultEventLoop);
        
        defaultEventLoop.execute(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            RuntimeException runtimeException = new RuntimeException("error");
            log.debug("error", runtimeException);
            promise.setFailure(runtimeException);
        });


        log.debug("start...");
        log.debug("{}", promise.getNow());
//        promise.get();

        promise.await(); // 与sync和get区别在于，不会抛异常
        log.debug("result {}", (promise.isSuccess() ? promise.getNow() : promise.cause()).toString());
    }

}
