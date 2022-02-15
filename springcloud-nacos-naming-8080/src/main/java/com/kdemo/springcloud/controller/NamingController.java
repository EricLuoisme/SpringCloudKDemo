package com.kdemo.springcloud.controller;

import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import com.alibaba.nacos.api.naming.pojo.Instance;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NamingController {

    @NacosInjected
    private NamingService namingService;


    @GetMapping("/getService/{name}")
    public List<Instance> getService(@PathVariable("name") String serviceName) throws NacosException {
        return namingService.getAllInstances(serviceName);
    }

    @GetMapping("/registry")
    public String registry() throws NacosException {
        Instance instance = new Instance();
        instance.setClusterName("test1");
        instance.setEnabled(true);
        instance.setEphemeral(true);
        instance.setHealthy(true);
        instance.setInstanceId("127.0.0.1");
        instance.setPort(8080);
        namingService.registerInstance("example", instance);
        return "Success";
    }


}
