package com.kdemo.springcloud.filter.rewriter.department;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdemo.springcloud.filter.FeignPathDto;
import com.kdemo.springcloud.filter.rewriter.AbstractRewriter;
import com.kdemo.springcloud.pojo.Department;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;


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
    public BiFunction<ServerWebExchange, List, Mono<List>> rewriteResponse() {
        return (exchange, list) -> {
            // need to use objectMapper rather than jackson
            ObjectMapper om = new ObjectMapper();
            List<Department> departments = om.convertValue(list, new TypeReference<>() {
            });
            // map to what we want
            return Mono.just(departments.stream()
                    .map(department -> DepartmentVo.builder()
                            .departmentId(department.getDept_no())
                            .departmentName(department.getDept_name())
                            .departmentSource(department.getDb_source())
                            .build())
                    .collect(Collectors.toList()));
        };
    }


    @Override
    public GatewayFilterSpec addingRequestRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec;
    }

    @Override
    public GatewayFilterSpec addingResponseRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec
                .modifyResponseBody(List.class, List.class,
                        MediaType.APPLICATION_JSON_VALUE, (exchange, a) -> rewriteResponse().apply(exchange, a));
    }
}
