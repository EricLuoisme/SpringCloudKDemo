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
public class DepartmentAddRewriter extends AbstractRewriter {

    @Override
    public String effectedFullPath() {
        return "/department/add";
    }

    @Override
    public BiFunction<ServerWebExchange, DepartmentVo, Mono<Department>> rewriteRequest() {
        return (exchange, departmentVo) -> {
            Department department = new Department();
            department.setDept_no(departmentVo.getDepartmentId());
            department.setDept_name(departmentVo.getDepartmentName());
            department.setDb_source(departmentVo.getDepartmentSource());
            return Mono.just(department);
        };
    }

    @Override
    public <O, I> BiFunction<ServerWebExchange, I, Mono<O>> rewriteResponse() {
        return null;
    }


    @Override
    public GatewayFilterSpec addingRequestRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec
                .modifyRequestBody(DepartmentVo.class, Department.class,
                        MediaType.APPLICATION_JSON_VALUE, createGenericRewriter(rewriteRequest()));
    }

    @Override
    public GatewayFilterSpec addingResponseRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec;
    }


}
