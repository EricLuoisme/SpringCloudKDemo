package com.kdemo.springcloud.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SSE Controller, we have to set them return stream value (media type), then we can get it continuity
 *
 * @author Roylic
 * 2022/6/8
 */
@RestController
@RequestMapping("/sse")
public class SseController {

    @Autowired
    private SseServiceHandler sseServiceHandler;


    /**
     * Reactive implementation of SSE
     */
    @GetMapping(value = "/testFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getFluxTest() {
        return Flux.interval(Duration.ofSeconds(3)).log();
    }


    /**
     * SSEEmitter implementation of SSE
     */
    @GetMapping(value = "/testEmit", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter getSseTest() {

        // assume that the request inside here should be authorised one

        // default connection to 10s
        SseEmitter emitter = new SseEmitter(10000L);

        // calling the service
        return sseServiceHandler.sseInsiderService(emitter);
    }

}
