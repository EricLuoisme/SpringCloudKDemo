package com.kdemo.springcloud.controller;

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
        SseEmitter emitter = new SseEmitter();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
        {
            try {
                for (int i = 0; i < 100; i++) {
                    TimeUnit.SECONDS.sleep(3);
                    emitter.send(i);
                }
                emitter.complete();
            } catch (IOException | InterruptedException e) {
                emitter.completeWithError(e);
            }
        });
        executor.shutdown();
        return emitter;
    }

}
