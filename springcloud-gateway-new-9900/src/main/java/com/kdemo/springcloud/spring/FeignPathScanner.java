package com.kdemo.springcloud.spring;

import feign.Feign;
import feign.RequestLine;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.reactive.result.method.RequestMappingInfo;
import org.springframework.web.reactive.result.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Roylic
 * 2023/3/21
 */
@Component
public class FeignPathScanner implements ApplicationContextAware {


    private ApplicationContext applicationContext;

    @Autowired
    private RequestMappingHandlerMapping handlerMapping;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void postConstruct() {
//        List<String> paths = new ArrayList<>();
//        FeignContext feignContext = applicationContext.getBean(FeignContext.class);
//        Set<String> contextNames = feignContext.getContextNames();
//        System.out.println();
//
//        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();
//        System.out.println();
//
//        String[] beanNamesForAnnotation = applicationContext.getBeanNamesForAnnotation(GetMapping.class);
//        System.out.println();


//        feignContext.getInstances().values().forEach(feignClientFactoryBean -> {
//            Class<?> targetType = feignClientFactoryBean.getType();
//            Object target = Feign.builder().target(targetType, "");
//            for (Method method : targetType.getDeclaredMethods()) {
//                if (method.isAnnotationPresent(annotationClass)) {
//                    String[] values = method.getAnnotation(RequestMapping.class).value();
//                    RequestLine requestLine = method.getAnnotation(RequestLine.class);
//                    String path = values.length > 0 ? values[0] : (requestLine != null ? requestLine.value() : "");
//                    paths.add(path);
//                }
//            }
//        });

        System.out.println();
    }


}
