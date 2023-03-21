package com.kdemo.springcloud.spring;

import com.kdemo.springcloud.config.RouteConfig;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * SmartInitializingSingleton demo
 *
 * @author Roylic
 * 2023/3/20
 */
@Slf4j
@Component
public class CustomSmartInitializingSingleton implements BeanClassLoaderAware, SmartInitializingSingleton {


    private List<String> feignPath = new LinkedList<>();

    private ClassLoader classLoader;

    private final ListableBeanFactory beanFactory;

    private final RouteConfig routeConfig;


    public CustomSmartInitializingSingleton(ListableBeanFactory beanFactory, RouteConfig routeConfig) {
        this.beanFactory = beanFactory;
        this.routeConfig = routeConfig;
    }


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SneakyThrows
    @Override
    public void afterSingletonsInstantiated() {
//        // traverse all feign path
//        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(FeignClient.class);
//        for (Map.Entry<String, Object> entry : beansWithAnnotation.entrySet()) {
//            String className = entry.getKey();
//            Class<?> aClass = ClassUtils.resolveClassName(className, this.classLoader);
//            for (Method declaredMethod : aClass.getDeclaredMethods()) {
//                if (declaredMethod.isAnnotationPresent(PostMapping.class)) {
//                    PostMapping annotation = declaredMethod.getAnnotation(PostMapping.class);
//                    printPathByProxy(Proxy.getInvocationHandler(annotation));
//                }
//                if (declaredMethod.isAnnotationPresent(GetMapping.class)) {
//                    GetMapping annotation = declaredMethod.getAnnotation(GetMapping.class);
//                    printPathByProxy(Proxy.getInvocationHandler(annotation));
//                }
//            }
//        }
//        // reset to the config
//        routeConfig.setAllFeignPaths(this.feignPath);
//        System.out.println();
    }

    private void printPathByProxy(InvocationHandler invocationHandler) throws NoSuchFieldException, IllegalAccessException {
        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
        // same result as using memberValues.setAccessible(true);
        ReflectionUtils.makeAccessible(memberValues);

        Map memberValuesMap = (Map) memberValues.get(invocationHandler);
        String[] values = (String[]) memberValuesMap.get("value");
        String[] paths = (String[]) memberValuesMap.get("path");
        String path = values.length == 0 ? paths[0] : values[0];
        feignPath.add(path);

        log.debug("Find feign path: " + path);
    }


}
