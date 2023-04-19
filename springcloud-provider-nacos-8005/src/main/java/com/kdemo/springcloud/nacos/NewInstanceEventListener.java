package com.kdemo.springcloud.nacos;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.nacos.client.naming.event.InstancesChangeEvent;
import com.alibaba.nacos.common.notify.Event;
import com.alibaba.nacos.common.notify.listener.Subscriber;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NewInstanceEventListener extends Subscriber<InstancesChangeEvent> {

    @Override
    public Class<? extends Event> subscribeType() {
        return InstancesChangeEvent.class;
    }

    @Override
    public void onEvent(InstancesChangeEvent event) {
        log.debug(">>> 8005 Received Nacos Event :{}", JSONObject.toJSONString(event));
        System.out.println(">>> 8005 Received Nacos Event :" + JSONObject.toJSONString(event));
    }
}
