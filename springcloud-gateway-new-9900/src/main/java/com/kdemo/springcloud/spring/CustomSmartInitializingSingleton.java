package com.kdemo.springcloud.spring;

import feign.ReflectiveFeign;
import lombok.SneakyThrows;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Map;

/**
 * SmartInitializingSingleton demo
 *
 * @author Roylic
 * 2023/3/20
 */
@Component
public class CustomSmartInitializingSingleton implements ApplicationContextAware, BeanClassLoaderAware, SmartInitializingSingleton {

    private ApplicationContext context;

    private ClassLoader classLoader;

    private final ListableBeanFactory beanFactory;


    public CustomSmartInitializingSingleton(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SneakyThrows
    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(FeignClient.class);
        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
            String className = entry.getKey();
            Class<?> aClass = ClassUtils.resolveClassName(className, this.classLoader);
            for (Method declaredMethod : aClass.getDeclaredMethods()) {
                if (declaredMethod.isAnnotationPresent(PostMapping.class)) {
                    PostMapping annotation = declaredMethod.getAnnotation(PostMapping.class);
                    printPathByProxy(Proxy.getInvocationHandler(annotation), false);
                }
                if (declaredMethod.isAnnotationPresent(GetMapping.class)) {
                    GetMapping annotation = declaredMethod.getAnnotation(GetMapping.class);
                    printPathByProxy(Proxy.getInvocationHandler(annotation), true);
                }
            }
        }
    }

    private void printPathByProxy(InvocationHandler invocationHandler, boolean isGet) throws NoSuchFieldException, IllegalAccessException {
        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
        ReflectionUtils.makeAccessible(memberValues);

        Map memberValuesMap = (Map) memberValues.get(invocationHandler);
        String[] values = (String[]) memberValuesMap.get("value");
        String[] paths = (String[]) memberValuesMap.get("path");
        System.out.println(values.length == 0 ? paths[0] : values[0]);
    }


}
