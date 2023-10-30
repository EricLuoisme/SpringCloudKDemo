package com.kdemo.springcloud.handler;

public class SentinelFallbackHandler {

    public static String handlerFallback(Throwable t) {
        return "Fallback due to: " + t.getClass().getSimpleName();
    }

    public static String defaultFallback() {
        return "Default Fallback";
    }
}
