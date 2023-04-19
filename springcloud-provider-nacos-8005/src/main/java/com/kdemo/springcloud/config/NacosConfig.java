package com.kdemo.springcloud.config;

import com.alibaba.nacos.common.notify.NotifyCenter;
import com.kdemo.springcloud.nacos.NewInstanceEventListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NacosConfig {

    public NacosConfig() {
        NotifyCenter.registerSubscriber(new NewInstanceEventListener());
    }
}
