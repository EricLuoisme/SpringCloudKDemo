package com.kdemo.springcloud;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
// 自动注册服务到服务中心, EnableEurekaClient默认就带上了EnableEureka
@EnableEurekaClient
// 添加熔断支持 (这个注释只是启动断路器)
@EnableCircuitBreaker
// 如EnableZuulProxy中描述, 生产者需要开启才可以被反向代理
@EnableDiscoveryClient
// 开启缓存才能用 Spring Cache
@EnableCaching
public class DeptProvider_Hystrix_8001 {
    public static void main(String[] args) {
        SpringApplication.run(DeptProvider_Hystrix_8001.class, args);
    }

    // 怎加一个Servlet的bean, 用于Hystrix dashboard监控
    @Bean
    public ServletRegistrationBean hystrixMetricsStreamServlet() {
        // 这段代码是固定的
        ServletRegistrationBean<HystrixMetricsStreamServlet> registrationBean
                = new ServletRegistrationBean<>(new HystrixMetricsStreamServlet());
        registrationBean.addUrlMappings("/actuator/hystrix.stream");
        return registrationBean;
    }
}
