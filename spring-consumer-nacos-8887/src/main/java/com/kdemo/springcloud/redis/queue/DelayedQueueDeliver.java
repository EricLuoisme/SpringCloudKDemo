package com.kdemo.springcloud.redis.queue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RDelayedQueue;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * Delay queue msg deliver
 */
@Slf4j
@Service
public class DelayedQueueDeliver {

    private static final String MSG_QUEUE_NAME = "MsgDelayQueue";

    private final RBlockingQueue<String> blockingQueue;

    private final RDelayedQueue<String> delayedQueue;
    private final HttpMessageConverters messageConverters;


    public DelayedQueueDeliver(RedissonClient redissonClient, HttpMessageConverters messageConverters) {
        this.blockingQueue = redissonClient.getBlockingQueue(MSG_QUEUE_NAME);
        this.delayedQueue = redissonClient.getDelayedQueue(this.blockingQueue);
        this.messageConverters = messageConverters;
    }


    /**
     * Add new message with delay consumption time (in seconds)
     *
     * @param msg            str msg
     * @param delayInSeconds delay seconds for consumption
     */
    public void addNewMessage(String msg, long delayInSeconds) {
        delayedQueue.offer(msg, delayInSeconds, TimeUnit.SECONDS);
        log.info("Message added to queue:{}, msg:{}", MSG_QUEUE_NAME, msg);
    }

}
