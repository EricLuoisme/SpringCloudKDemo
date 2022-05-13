package com.kdemo.springcloud.grpc;

import com.alibaba.nacos.grpc.GrpcServer;
import com.alibaba.nacos.grpc.utils.NacosUtils;
import com.kdemo.springcloud.protos.GrpcNacosOptions;
import com.kdemo.springcloud.protos.GrpcNacosProto;
import io.grpc.BindableService;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.util.Properties;

/**
 * 注册到Nacos上的grpc服务
 *
 * @author Roylic
 * 2022/5/13
 */

@Slf4j
public class NacosGrpcServer {

    GrpcServer server;

    public static void main(String[] args) throws InterruptedException {
        final NacosGrpcServer theServer = new NacosGrpcServer();
        // bindable services are input as array
        theServer.grpcServerStart(new RemoteRpcServiceImpl[]{new RemoteRpcServiceImpl()});
        // block
        theServer.server.blockUtilShutdown();
    }

    private void grpcServerStart(BindableService[] bindableServices) {
        server = new GrpcServer();
        // by extends the MessageOption, get nacos configs
        int port = GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.grpcNacosPort);
        URI uri = URI.create(GrpcNacosOptions.getDescriptor().getOptions().getExtension(GrpcNacosProto.nacosUri));
        // build as properties
        Properties properties = new Properties();
        properties = NacosUtils.buildNacosProperties(uri, properties);
        // use properties to start GrpcNettyServer with nacos configurations
        server.init(port, properties, bindableServices);
        server.start();
        // safely shutting down grpc netty server before jvm shutting down
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            server.stop();
            System.err.println("*** server shut down");
        }));
    }

}
