package com.kdemo.springcloud.redis.script;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

/**
 * Delay queue implementation with Redisson
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisDelayConsumptionTest {


    private static final String BLOCK_QUEUE_NAME = "BlockingQueue";

    @Autowired
    private RedissonClient redissonClient;


    @Test
    public void delayQueueLogic() throws InterruptedException {

        // queues initialization
        RBlockingQueue<Object> blockingQueue = redissonClient.getBlockingQueue(BLOCK_QUEUE_NAME, StringCodec.INSTANCE);

        // delay queue need blocking queue as basic
        RDelayedQueue<Object> delayedQueue = redissonClient.getDelayedQueue(blockingQueue);

        // msg offering
        String msg = "happy";
        long delay = 3;
        delayedQueue.offer(msg, delay, TimeUnit.SECONDS);
        log.info("Msg added to delayQueue: {}", msg);


        // msg consumption
        new Thread(() -> {
            try {
                log.debug("Waiting for a message...");
                while (true) {
                    Object receive = blockingQueue.take();
                    log.info("Receive Msg:{}", receive);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        }).start();

        // main thread waiting
        Thread.sleep(TimeUnit.SECONDS.toMillis(10));

        delayedQueue.destroy();
        redissonClient.shutdown();
    }


}
