package com.kdemo.springcloud.spring;

import com.kdemo.springcloud.dto.FeignPathDto;
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
import org.springframework.web.bind.annotation.RequestMapping;

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

    private final List<FeignPathDto> feignPathDtoList = new LinkedList<>();


    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public List<FeignPathDto> getFeignPathDtoList() {
        return feignPathDtoList;
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
                if (aClass.isAnnotationPresent(FeignClient.class)) {
                    // get Feign Service Name
                    String feignServiceName = getFeignServiceNameByProxy(Proxy.getInvocationHandler(aClass.getAnnotation(FeignClient.class)));
                    // get Request Mapping path
                    String baseUrl = aClass.isAnnotationPresent(RequestMapping.class)
                            ? getMappingPathByProxy(Proxy.getInvocationHandler(aClass.getAnnotation(RequestMapping.class)))
                            : "";
                    // get PostMapping + GetMapping path
                    for (Method declaredMethod : aClass.getDeclaredMethods()) {
                        if (declaredMethod.isAnnotationPresent(PostMapping.class)) {
                            String path = getMappingPathByProxy(Proxy.getInvocationHandler(declaredMethod.getAnnotation(PostMapping.class)));
                            feignPathDtoList.add(FeignPathDto.builder().fullPath(baseUrl + path).serverName(feignServiceName).build());
                        }
                        if (declaredMethod.isAnnotationPresent(GetMapping.class)) {
                            String path = getMappingPathByProxy(Proxy.getInvocationHandler(declaredMethod.getAnnotation(GetMapping.class)));
                            feignPathDtoList.add(FeignPathDto.builder().fullPath(baseUrl + path).serverName(feignServiceName).build());
                        }
                    }
                }
            }
        }

        feignPathDtoList.forEach(dto -> System.out.println("Scanned feign with path: " + dto.getFullPath() + ", with service name: " + dto.getServerName()));
    }

    private static String getFeignServiceNameByProxy(InvocationHandler invocationHandler) throws NoSuchFieldException, IllegalAccessException {
        Map memberValuesMap = accessMemberValues(invocationHandler);
        String value = (String) memberValuesMap.get("value");
        String name = (String) memberValuesMap.get("name");
        return StringUtils.hasLength(value) ? value : name;
    }

    private static String getMappingPathByProxy(InvocationHandler invocationHandler) throws NoSuchFieldException, IllegalAccessException {
        Map memberValuesMap = accessMemberValues(invocationHandler);
        String[] values = (String[]) memberValuesMap.get("value");
        String[] paths = (String[]) memberValuesMap.get("path");
        return values.length == 0 ? paths[0] : values[0];
    }

    private static Map accessMemberValues(InvocationHandler invocationHandler) throws NoSuchFieldException, IllegalAccessException {
        Field memberValues = invocationHandler.getClass().getDeclaredField("memberValues");
        // same result as using memberValues.setAccessible(true);
        ReflectionUtils.makeAccessible(memberValues);
        return (Map) memberValues.get(invocationHandler);
    }


}
