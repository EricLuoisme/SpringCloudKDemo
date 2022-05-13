package com.kdemo.springcloud.grpc;

import net.devh.boot.grpc.server.interceptor.GrpcGlobalServerInterceptor;
import org.springframework.context.annotation.Configuration;

/**
 * Grpc-Nacos 配置
 *
 * @author Roylic
 * 2022/5/13
 */
@Configuration(proxyBeanMethods = false)
public class GrpcConfig {

    // add the interceptor
    @GrpcGlobalServerInterceptor
    GrpcLogInterceptor grpcInterceptor() {
        return new GrpcLogInterceptor();
    }

}
