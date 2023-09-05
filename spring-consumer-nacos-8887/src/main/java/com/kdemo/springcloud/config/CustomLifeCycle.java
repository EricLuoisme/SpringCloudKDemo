package com.kdemo.springcloud.config;

import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.cloud.nacos.discovery.NacosWatch;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingMaintainService;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Integrate with Spring Smart Lifecycle -> when it starts or stop
 */
@Slf4j
@Component
public class CustomLifeCycle implements SmartLifecycle {

    // use this to indicate -> on this service running status on Nacos
    private final AtomicBoolean isRunning = new AtomicBoolean(false);

    @Value("${server.port}")
    private int port;

    @Value("${spring.application.name}")
    private String serverAppNames;

    @Autowired
    private NacosWatch nacosWatch;

    @Autowired
    private NacosServiceManager nacosServiceManager;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;


    @Override
    public void start() {
        log.debug("Start with the SmartLifecycle");
        isRunning.set(true);
    }

    @Override
    public void stop() {
        log.debug("Stop with the SmartLifecycle");
        isRunning.set(false);
        nacosWatch.stop();
        try {
            // gracefully stop de-register from Nacos
            Instance instance = new Instance();
            instance.setIp(Inet4Address.getLocalHost().getHostAddress());
            instance.setPort(port);
            instance.setServiceName(serverAppNames);
            // deregister
            NamingService namingService = nacosServiceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
            namingService.deregisterInstance(serverAppNames, instance);
            namingService.shutDown();
        } catch (UnknownHostException | NacosException e) {
            log.error("Error during shut down", e);
        }
    }

    @Override
    public boolean isRunning() {
        return isRunning.get();
    }
}
