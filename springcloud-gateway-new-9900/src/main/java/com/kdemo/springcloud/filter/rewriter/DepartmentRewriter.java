package com.kdemo.springcloud.filter.rewriter;

import com.kdemo.springcloud.dto.DepartmentVo;
import com.kdemo.springcloud.dto.FeignPathDto;
import com.kdemo.springcloud.pojo.Department;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Component
public class DepartmentRewriter extends AbstractRewriter implements RewriteFunction<DepartmentVo, Department> {

    @Override
    public String effectedFullPath() {
        return "/department/add";
    }

    @Override
    public Publisher<Department> apply(ServerWebExchange serverWebExchange, DepartmentVo departmentVo) {
        Department department = new Department();
        department.setDept_no(departmentVo.getDepartmentId());
        department.setDept_name(departmentVo.getDepartmentName());
        department.setDb_source(departmentVo.getDepartmentSource());
        return Mono.just(department);
    }

    @Override
    public GatewayFilterSpec addingRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec.modifyRequestBody(
                DepartmentVo.class, Department.class, MediaType.APPLICATION_JSON_VALUE, new DepartmentRewriter());
    }

}
