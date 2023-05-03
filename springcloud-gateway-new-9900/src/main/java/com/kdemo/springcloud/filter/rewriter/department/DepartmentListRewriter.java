package com.kdemo.springcloud.filter.rewriter.department;

import com.kdemo.springcloud.filter.FeignPathDto;
import com.kdemo.springcloud.filter.rewriter.AbstractRewriter;
import com.kdemo.springcloud.pojo.Department;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;


@Component
public class DepartmentListRewriter extends AbstractRewriter {

    @Override
    public String effectedFullPath() {
        return "/department/list";
    }

    @Override
    public <O, I> BiFunction<ServerWebExchange, O, Mono<I>> rewriteRequest() {
        return null;
    }

    @Override
    public BiFunction<ServerWebExchange, Department, Mono<DepartmentVo>> rewriteResponse() {
        return (exchange, department) -> Mono.just(
                DepartmentVo.builder()
                        .departmentId(department.getDept_no())
                        .departmentName(department.getDept_name())
                        .departmentSource(department.getDb_source())
                        .build());
    }


    @Override
    public GatewayFilterSpec addingRequestRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec;
    }

    @Override
    public GatewayFilterSpec addingResponseRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        // TODO not working for list response for now
        return gatewayFilterSpec
                .modifyResponseBody(Department.class, DepartmentVo.class,
                        MediaType.APPLICATION_JSON_VALUE, createGenericRewriter(rewriteResponse()));
    }


}
