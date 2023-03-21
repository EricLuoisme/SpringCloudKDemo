package com.kdemo.springcloud.spring;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * BeanFactoryPostProcessor demo
 *
 * @author Roylic
 * 2023/3/20
 */
@Slf4j
@Component
public class CustomBeanFactoryPostProcessor implements BeanClassLoaderAware, BeanFactoryPostProcessor {

    private ClassLoader classLoader;

    private final List<String> pathList = new LinkedList<>();


    public List<String> getPathList() {
        return pathList;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SneakyThrows
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // traverse
        for (String beanDefinitionName : beanFactory.getBeanDefinitionNames()) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanDefinitionName);
            String beanClassName = beanDefinition.getBeanClassName();
            if (null != beanClassName) {
                Class<?> aClass = ClassUtils.resolveClassName(beanClassName, this.classLoader);
                // traverse all feign path
                if (aClass.isAnnotationPresent(FeignClient.class) || aClass.getSuperclass().isAnnotationPresent(FeignClient.class)) {

                    FeignClient feignClient = aClass.getAnnotation(FeignClient.class);
                    String feignServiceName = getFeignServiceNameByProxy(Proxy.getInvocationHandler(feignClient));

                    // TODO 针对interface上可能还有requestMapping注解, 需要结合

                    for (Method declaredMethod : aClass.getDeclaredMethods()) {
                        if (declaredMethod.isAnnotationPresent(PostMapping.class)) {
                            PostMapping annotation = declaredMethod.getAnnotation(PostMapping.class);
                            getMappingPathByProxy(Proxy.getInvocationHandler(annotation));
                        }
                        if (declaredMethod.isAnnotationPresent(GetMapping.class)) {
                            GetMapping annotation = declaredMethod.getAnnotation(GetMapping.class);
                            getMappingPathByProxy(Proxy.getInvocationHandler(annotation));
                        }
                    }
                }
            }
        }
        System.out.println();
    }


    private String getFeignServiceNameByProxy(InvocationHandler invocationHandler) throws NoSuchFieldException, IllegalAccessException {
        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
        // same result as using memberValues.setAccessible(true);
        ReflectionUtils.makeAccessible(memberValues);

        Map memberValuesMap = (Map) memberValues.get(invocationHandler);
        String value = (String) memberValuesMap.get("value");
        String name = (String) memberValuesMap.get("name");
        return StringUtils.hasLength(value) ? value : name;
    }


    private void getMappingPathByProxy(InvocationHandler invocationHandler) throws NoSuchFieldException, IllegalAccessException {
        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
        // same result as using memberValues.setAccessible(true);
        ReflectionUtils.makeAccessible(memberValues);

        Map memberValuesMap = (Map) memberValues.get(invocationHandler);
        String[] values = (String[]) memberValuesMap.get("value");
        String[] paths = (String[]) memberValuesMap.get("path");
        String path = values.length == 0 ? paths[0] : values[0];
        pathList.add(path);

        log.debug("Find feign path: " + path);
    }


}
