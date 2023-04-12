package com.kdemo.springcloud.grpc;


import io.grpc.Server;
import io.grpc.netty.NettyServerBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * @author Roylic
 * @date 2022/4/28
 */
@Slf4j
public class SimpleGrpcServer {

    public static void main(String[] args) {
        Server grpcNettyServer = NettyServerBuilder
                .forPort(9090)
//                .addService(new RemoteRpcServiceImpl())
                .build();
        try {
            grpcNettyServer.start();
            grpcNettyServer.awaitTermination();
            log.info("Grpc Netty Server Start Successfully on port:{}", grpcNettyServer.getPort());
        } catch (IOException | InterruptedException e) {
            log.error("Grpc Netty Server Stop");
            e.printStackTrace();
        }
    }
}
