package com.kdemo.springcloud.filter;

import com.kdemo.springcloud.dto.DepartmentVo;
import com.kdemo.springcloud.pojo.Department;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.factory.rewrite.RewriteFunction;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Rewrite the body
 */
public class BodyRewriter {

    public static class DepartmentRewriter implements RewriteFunction<DepartmentVo, Department> {
        @Override
        public Publisher<Department> apply(ServerWebExchange serverWebExchange, DepartmentVo departmentVo) {
            Department department = new Department();
            department.setDept_no(departmentVo.getDepartmentId());
            department.setDept_name(departmentVo.getDepartmentName());
            department.setDb_source(department.getDb_source());
            return Mono.just(department);
        }
    }
}
