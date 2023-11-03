package com.kdemo.springcloud.sse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * SSE Controller, we have to set them return stream value (media type), then we can get it continuity
 *
 * @author Roylic
 * 2022/6/8
 */
@RestController
@RequestMapping("/sse")
public class OwnSseController {

    @Autowired
    private SseServiceHandler sseServiceHandler;


    /**
     * Reactive implementation of SSE
     */
    @GetMapping(value = "/testFlux", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Long> getFluxTest() {
        return Flux.interval(Duration.ofSeconds(3))
                // this timeout is for cancel the Flux when it's interval exceed this config
                .timeout(Duration.ofSeconds(5))
                // this timeout is the one that could terminate the stream
                .take(Duration.ofSeconds(10))
                .log();
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
