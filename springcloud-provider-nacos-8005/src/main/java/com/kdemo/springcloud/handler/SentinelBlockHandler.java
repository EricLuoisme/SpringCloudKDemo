package com.kdemo.springcloud.handler;

import com.alibaba.csp.sentinel.slots.block.BlockException;

public class SentinelBlockHandler {

    public static String handlerBlock(BlockException ex) {
        return "Blocked by Sentinel: " + ex.getCause().getMessage();
    }

}
