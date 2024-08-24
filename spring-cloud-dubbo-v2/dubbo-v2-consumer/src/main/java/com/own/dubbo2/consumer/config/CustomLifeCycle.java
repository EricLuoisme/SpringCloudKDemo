package com.own.dubbo2.consumer.config;

import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.serialize.Serialization;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;


/**
 * Bug found & elimination
 * 1) Dubbo is lazy initialized, after application started, it remains no SPI loaded
 * 2) Then first dubbo call (request to other dubbo service), is using 'initialized once' strategy
 * 3) Inner loading the SPI, Dubbo 2.7 use Thread.currentThread.getContextClassLoader()
 * <p>
 * 4) If the ForkJoinPool's thread call 'scramble' the opportunity -> it cause SPI not found
 * - because for ForkJoinPool, it's classLoader is AppClassLoader
 * - but for normal (Spring handled) thread, that would be -> LaunchedURLClassLoader (or TomcatEmbeddedWebappClassLoader)
 * <p>
 * 5) A feasible workaround would be: use the correct thread to 'scramble' the opportunity first,
 * by integrating the SmartLifecycle, use Spring-Handled thread to call
 * ExtensionLoader.getExtensionLoader(LoadBalance.class)
 * .getDefaultExtensionName();
 * could init the 'cached' placed with is 'initialized once', not letting ForkJoinPool even touch this
 * <p>
 * <p>
 * Extra Bug:
 * - By doing above stuff, we also need to manually config:
 * dubbo.protocol.serialization: hessian2
 * Even the hessian2 is default serialization method, not manually config it could cause later
 * serialization SPI not found
 * - Or else, we need pre-load it like what we do with the loadBalance.class
 */
@Slf4j
@Configuration
public class CustomLifeCycle implements SmartLifecycle {

    @Override
    public void start() {
        Thread thread = Thread.currentThread();
        Class<? extends CustomLifeCycle> aClass = getClass();
        ClassLoader classLoader = aClass.getClassLoader();

        // load dubbo config just after start, eliminate the forkJoinPool's
        // thread with different class loader cause issue
        log.debug("[CustomSmartLifeCycle] preloading start, thread name:{}, thread context classloader:{}, " +
                "classes classLoader:{}", thread.getName(), thread.getContextClassLoader(), classLoader);
        // must add for loadBalance
        ExtensionLoader.getExtensionLoader(LoadBalance.class).getDefaultExtensionName();
        // need to add, if we not config: dubbo.protocol.serialization=hessian2
//        ExtensionLoader.getExtensionLoader(Serialization.class).getDefaultExtensionName();
        log.debug("[CustomSmartLifeCycle] preloading finished");
    }

    @Override
    public void stop() {
    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
