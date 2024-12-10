package com.kdemo.springcloud.redis.queue;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;


/**
 * Delay queue msg receiver
 */
@Slf4j
@Service
public class DelayedQueueReceiver {

    private static final String MSG_QUEUE_NAME = "MsgDelayQueue";

    private final RBlockingQueue<String> blockingQueue;


    public DelayedQueueReceiver(RedissonClient redissonClient) {
        this.blockingQueue = redissonClient.getBlockingQueue(MSG_QUEUE_NAME);
    }


    @PostConstruct
    public void postConstruct() {
        Thread thread = new Thread(this::iterLogic);
        thread.start();
    }

    /**
     * Looping for retrieving msg from blocking queue
     */
    private void iterLogic() {
        log.info("[DelayedQueueReceiver] start iter logic");
        while (true) {
            try {
                String receiveMsg = blockingQueue.take();
                log.info("[DelayedQueueReceiver] Message got from queue:{}, msg:{}", MSG_QUEUE_NAME, receiveMsg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
