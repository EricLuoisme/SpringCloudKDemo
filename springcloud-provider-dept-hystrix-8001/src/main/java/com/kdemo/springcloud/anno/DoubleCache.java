package com.kdemo.springcloud.anno;

import java.lang.annotation.*;

/**
 * 使用注解处理Caffeine+Redis的多级缓存
 *
 * @author Roylic
 * 2022/5/17
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DoubleCache {

    // 缓存key
    String cacheName();

    // 支持springEl表达式
    String key();

    // 二级缓存时常
    long l2TimeOut() default 120;

    // 缓存类型
    CacheType type() default CacheType.FULL;
}
