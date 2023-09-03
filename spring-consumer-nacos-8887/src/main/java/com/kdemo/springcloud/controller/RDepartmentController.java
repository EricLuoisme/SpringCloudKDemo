package com.kdemo.springcloud.controller;

import com.kdemo.springcloud.pojo.Department;
import com.kdemo.springcloud.service.DeptClientService;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/remote")
public class RDepartmentController {

    private final Counter listCounter;

    private final DeptClientService deptClientService;

    public RDepartmentController(MeterRegistry meterRegistry, DeptClientService deptClientService) {
        this.deptClientService = deptClientService;
        this.listCounter = Counter.builder("ep_counter_list_dept")
                .description("number of request on dept list")
                .register(meterRegistry);
    }

    @PostMapping("/list")
    @Timed(value = "endpoint_request_duration_dept_list", description = "Time consumption on listing department", histogram = true)
    public List<Department> getRemoteDeptList() {
        this.listCounter.increment();
        return deptClientService.findAll();
    }

    // does not work -> feign not support async request
    @PostMapping("/list/async")
    public CompletableFuture<List<Department>> getRemoteDeptList_Async() {
        return deptClientService.findAll_async();
    }

}
