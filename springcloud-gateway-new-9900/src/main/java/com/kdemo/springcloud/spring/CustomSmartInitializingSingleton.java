package com.kdemo.springcloud.spring;

import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * SmartInitializingSingleton demo
 *
 * @author Roylic
 * 2023/3/20
 */
@Component
public class CustomSmartInitializingSingleton implements SmartInitializingSingleton {

    private final ListableBeanFactory beanFactory;

    public CustomSmartInitializingSingleton(ListableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, Object> beansWithAnnotation = beanFactory.getBeansWithAnnotation(FeignClient.class);
        for (Map.Entry<String, Object> stringObjectEntry : beansWithAnnotation.entrySet()) {
            System.out.println();
        }
    }
}
