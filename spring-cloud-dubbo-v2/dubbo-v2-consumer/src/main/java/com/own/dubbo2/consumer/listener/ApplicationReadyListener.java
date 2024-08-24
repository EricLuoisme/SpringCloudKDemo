package com.own.dubbo2.consumer.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationReadyListener {

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        Thread thread = Thread.currentThread();
        log.info("[ApplicationReadyListener] thread name:{}, classloader:{}",
                thread.getName(), thread.getContextClassLoader());
    }

}
