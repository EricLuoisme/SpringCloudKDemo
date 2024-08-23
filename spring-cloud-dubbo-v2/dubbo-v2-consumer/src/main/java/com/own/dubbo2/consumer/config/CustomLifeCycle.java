package com.own.dubbo2.consumer.config;

import lombok.extern.slf4j.Slf4j;

import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class CustomLifeCycle implements SmartLifecycle {

    private ApplicationContext applicationContext;

    @Autowired
    public void ClassLoaderInspector(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void start() {
        // load dubbo config just after start, eliminate the forkJoinPool's
        // thread with different class loader cause issue
        log.debug("[CustomSmartLifeCycle] preloading start");
        ExtensionLoader.getExtensionLoader(LoadBalance.class)
                .getDefaultExtensionName();
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
