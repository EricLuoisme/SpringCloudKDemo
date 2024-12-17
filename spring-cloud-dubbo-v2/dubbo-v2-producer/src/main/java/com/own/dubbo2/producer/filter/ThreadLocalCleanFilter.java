package com.own.dubbo2.producer.filter;

import com.own.dubbo2.producer.thread.ThreadLocalHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

    @Slf4j
    @Activate(group = CommonConstants.PROVIDER, order = 1)
    public class ThreadLocalCleanFilter implements Filter {
        @Override
        public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
            try {
                // basically do nothing, pass the invocation
                return invoker.invoke(invocation);
            } finally {
                // but after finished (stack back to this frame), clear all threadLocal stuff
                log.info("Clear thread local for thread:{}", Thread.currentThread().getName());
                System.out.println("Clear clear clear");
                ThreadLocalHolder.clear();
            }
        }
    }
