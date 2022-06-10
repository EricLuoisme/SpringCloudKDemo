package com.kdemo.springcloud.sse;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * SSE 处理类
 *
 * @author Roylic
 * 2022/6/9
 */
@Service
public class SseServiceHandler {


    public SseEmitter sseInsiderService(SseEmitter emitter) {



        // here should be inside the service impl instead of controller
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() ->
        {
            try {
                for (int i = 0; i < 100; i++) {
                    TimeUnit.SECONDS.sleep(3);
                    emitter.send(
                            SseEmitter.event()
                                    .data("")
                                    .name(""));
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
