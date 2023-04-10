package com.kdemo.springcloud.filter.rewriter;

import com.kdemo.springcloud.dto.DepartmentVo;
import com.kdemo.springcloud.dto.FeignPathDto;
import com.kdemo.springcloud.pojo.Department;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;


@Component
public class DepartmentGetIdRewriter extends AbstractRewriter {

    @Override
    public String effectedFullPath() {
        return "/department/get/{id}";
    }

    @Override
    public BiFunction<ServerWebExchange, DepartmentVo, Mono<Department>> rewriteRequest() {
        return (serverWebExchange, departmentVo) -> null;
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
        return gatewayFilterSpec
                .modifyRequestBody(DepartmentVo.class, Department.class,
                        MediaType.APPLICATION_JSON_VALUE, createGenericRewriter(rewriteRequest()));
    }

    @Override
    public GatewayFilterSpec addingResponseRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec
                .modifyResponseBody(Department.class, DepartmentVo.class,
                        MediaType.APPLICATION_JSON_VALUE, createGenericRewriter(rewriteResponse()));
    }


}
