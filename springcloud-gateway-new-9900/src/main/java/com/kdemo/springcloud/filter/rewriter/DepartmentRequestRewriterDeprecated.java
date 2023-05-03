package com.kdemo.springcloud.filter.rewriter;

import com.kdemo.springcloud.filter.rewriter.department.DepartmentVo;
import com.kdemo.springcloud.filter.FeignPathDto;
import com.kdemo.springcloud.pojo.Department;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.cloud.gateway.route.builder.GatewayFilterSpec;
import org.springframework.http.MediaType;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;


@Deprecated
public class DepartmentRequestRewriterDeprecated implements RewriteFunction<DepartmentVo, Department> {


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

    public GatewayFilterSpec addingRewriter(GatewayFilterSpec gatewayFilterSpec, FeignPathDto dto) {
        return gatewayFilterSpec.modifyRequestBody(
                DepartmentVo.class, Department.class, MediaType.APPLICATION_JSON_VALUE, new DepartmentRequestRewriterDeprecated());
    }

}
