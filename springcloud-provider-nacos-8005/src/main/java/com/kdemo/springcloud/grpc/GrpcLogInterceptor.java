package com.kdemo.springcloud.grpc;

import io.grpc.Metadata;
import io.grpc.ServerCall;
import io.grpc.ServerCallHandler;
import io.grpc.ServerInterceptor;
import lombok.extern.slf4j.Slf4j;

/**
 * 对grpc提供的服务端interceptor接口进行实现,
 * 主要将请求内容与名称对应, 然后调用对应本地方法
 *
 * @author Roylic
 * 2022/5/13
 */
@Slf4j
public class GrpcLogInterceptor implements ServerInterceptor {
    @Override
    public <ReqT, RespT> ServerCall.Listener<ReqT> interceptCall(ServerCall<ReqT, RespT> call,
                                                                 Metadata headers,
                                                                 ServerCallHandler<ReqT, RespT> next) {

        log.info(">>> Method Call with full method name : " + call.getMethodDescriptor().getFullMethodName());
        return next.startCall(call, headers);
    }
}
