package com.kdemo.springcloud.grpc;

import com.kdemo.springcloud.protos.EmptyRequest;
import com.kdemo.springcloud.protos.RemoteRpcServiceGrpc;
import com.kdemo.springcloud.protos.RpcEmployee;
import com.kdemo.springcloud.protos.RpcEmployees;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.List;

/**
 * GRPC 单独Client启动
 *
 * @author Roylic
 * @date 2022/4/25
 */
public class SimpleGrpcClient {
    public static void main(String[] args) {

        // 建立与服务端沟通的Channel
        ManagedChannel channel = ManagedChannelBuilder
                .forAddress("127.0.0.1", 9090)
                .usePlaintext().build();

        // 将Channel建立为stub
        RemoteRpcServiceGrpc.RemoteRpcServiceBlockingStub stub = RemoteRpcServiceGrpc.newBlockingStub(channel);

        // 使用stub进行远程服务调用
        RpcEmployees employees = stub.getEmployees(EmptyRequest.newBuilder().build());
        // 遍历结果
        List<RpcEmployee> employeeList = employees.getEmployeeList();
        employeeList.forEach(rpcEmployee -> {
            System.out.println(">>> Id:" + rpcEmployee.getId());
            System.out.println(">>> Name:" + rpcEmployee.getName());
        });

        // 关闭client端链接
        channel.shutdown();
    }
}
