//package com.kdemo.springcloud.grpc;
//
//import com.kdemo.springcloud.protos.EmptyRequest;
//import com.kdemo.springcloud.protos.IdRequest;
//import com.kdemo.springcloud.protos.RemoteRpcServiceGrpc;
//import com.kdemo.springcloud.protos.RpcEmployee;
//import com.kdemo.springcloud.protos.RpcEmployees;
//import io.grpc.stub.StreamObserver;
//import net.devh.boot.grpc.server.service.GrpcService;
//
///**
// * 作为服务端, 对rpc的service进行具体服务逻辑实现
// *
// * @author Roylic
// * @date 2022/4/25
// */
//@GrpcService
//public class RemoteRpcServiceImpl extends RemoteRpcServiceGrpc.RemoteRpcServiceImplBase {
//
//    @Override
//    public void getEmployees(EmptyRequest request, StreamObserver<RpcEmployees> responseObserver) {
//        responseObserver.onNext(
//                RpcEmployees.newBuilder()
//                        .addEmployee(
//                                RpcEmployee.newBuilder()
//                                        .setId(1)
//                                        .setName("Emp1")
//                                        .setSalary(4000.55F).build())
//                        .addEmployee(
//                                RpcEmployee.newBuilder()
//                                        .setId(2)
//                                        .setName("Emp2")
//                                        .setSalary(3403.01F).build())
//                        .build());
//        responseObserver.onCompleted();
//
//    }
//
//    @Override
//    public void getEmployeeStream(IdRequest request, StreamObserver<RpcEmployee> responseObserver) {
//        super.getEmployeeStream(request, responseObserver);
//    }
//
//    @Override
//    public StreamObserver<RpcEmployee> getBidEmployeeStream(StreamObserver<RpcEmployee> responseObserver) {
//        return super.getBidEmployeeStream(responseObserver);
//    }
//}
