package com.own.dubbo2.producer.thread;

/**
 * Simulation for holding ThreadLocal
 */
public class ThreadLocalHolder {
    private static final ThreadLocal<Object> CONTEXT = new ThreadLocal<>();

    public static void set(Object val) {
        CONTEXT.set(val);
    }

    public static Object get() {
        return CONTEXT.get();
    }

    public static void clear() {
        CONTEXT.remove();
    }
}
