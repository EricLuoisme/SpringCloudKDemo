package com.kdemo.springcloud.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LocalScheduler {

//    @Scheduled(cron = "0/10 * * * * ?")
    public void logging() {
        log.info("Logging, Logging");
    }

}
