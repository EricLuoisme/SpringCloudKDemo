package com.kdemo.springcloud.oksse;

import okhttp3.*;
import okhttp3.internal.sse.RealEventSource;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;

/**
 * 使用OkHttp-Sse模拟客户端进行SSE请求
 *
 * @author Roylic
 * 2022/6/8
 */
public class OkhttpSseRequest {

    private static Request REQUEST = new Request.Builder()
            .url("http:127.0.01:8005/sse/testEmit")
            .build();

    private static final OkHttpClient OK_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build();


    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();

        RealEventSource realEventSource = new RealEventSource(REQUEST, new EventSourceListener() {

            @Override
            public void onOpen(@NotNull EventSource eventSource, @NotNull Response response) {
                System.out.println(">>> Connected");
            }

            @Override
            public void onClosed(@NotNull EventSource eventSource) {
                System.out.println("<<< Disconnected");
                completableFuture.complete(true);
            }

            @Override
            public void onEvent(@NotNull EventSource eventSource, @Nullable String id, @Nullable String type, @NotNull String data) {
                System.out.println("<<<< Received Data: " + data);
            }

            @Override
            public void onFailure(@NotNull EventSource eventSource, @Nullable Throwable t, @Nullable Response response) {
                System.out.println("<<<< Error ");
            }
        });

        // start connection
        realEventSource.connect(OK_CLIENT);

        Boolean isClosed = completableFuture.get();
        if (isClosed) {
            System.out.println(" Closed SSE ");
            // for perfect cancel
            realEventSource.cancel();
            OK_CLIENT.dispatcher().cancelAll();
        }


    }

}
