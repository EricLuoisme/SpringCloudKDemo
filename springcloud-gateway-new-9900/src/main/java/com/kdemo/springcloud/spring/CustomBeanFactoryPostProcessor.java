package com.kdemo.springcloud.spring;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;

/**
 * BeanFactoryPostProcessor demo
 *
 * @author Roylic
 * 2023/3/20
 */
@Component
public class CustomBeanFactoryPostProcessor implements BeanClassLoaderAware, BeanFactoryPostProcessor {

    @Autowired
    private GatewayProperties gatewayProperties;

    private ClassLoader classLoader;

    private List<String> pathList = new LinkedList<>();


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (null != beanClassName) {
                Class<?> aClass = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                if (aClass.isAnnotationPresent(FeignClient.class) || aClass.getSuperclass().isAnnotationPresent(FeignClient.class)) {
                    for (Method declaredMethod : aClass.getDeclaredMethods()) {
                        if (declaredMethod.isAnnotationPresent(PostMapping.class)) {
                            PostMapping annotation = declaredMethod.getAnnotation(PostMapping.class);
                            String[] value = annotation.value();
                            System.out.println();
                        }
                        if (declaredMethod.isAnnotationPresent(GetMapping.class)) {
                            GetMapping annotation = declaredMethod.getAnnotation(GetMapping.class);
                            String[] value = annotation.value();
                            System.out.println();
                        }
                    }
                }
            }
        }

        pathList.forEach(path -> {
            RouteDefinition routeDefinition = new RouteDefinition();
            routeDefinition.setId("1245");
            routeDefinition.setUri(URI.create(path));
        });
    }


}
