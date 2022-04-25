package com.kdemo.springcloud;


import com.kdemo.springcloud.protos.*;
import io.grpc.stub.StreamObserver;

/**
 * 作为服务端, 对rpc的service进行具体服务逻辑实现
 *
 * @author Roylic
 * @date 2022/4/25
 */
public class RemoteRpcServiceImpl extends RemoteRpcServiceGrpc.RemoteRpcServiceImplBase {


    @Override
    public void getEmployees(EmptyRequest request, StreamObserver<RpcEmployees> responseObserver) {
        super.getEmployees(request, responseObserver);
    }

    @Override
    public void getEmployeeStream(IdRequest request, StreamObserver<RpcEmployee> responseObserver) {
        super.getEmployeeStream(request, responseObserver);
    }

    @Override
    public StreamObserver<RpcEmployee> getBidEmployeeStream(StreamObserver<RpcEmployee> responseObserver) {
        return super.getBidEmployeeStream(responseObserver);
    }
}
