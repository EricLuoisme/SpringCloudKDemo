package com.kdemo.health;

import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.Server;

/**
 * 自实现心跳检测
 */
public class HealthChecker implements IPing {

    @Override
    public boolean isAlive(Server server) {
        /**
         * 这里的逻辑是访问Producer提供的一个对外API, 若返回200, 就认为是OK
         */
        return false;
    }
}
