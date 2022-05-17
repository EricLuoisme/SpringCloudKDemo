package com.kdemo.springcloud.anno;

import com.github.benmanes.caffeine.cache.Cache;
import com.kdemo.springcloud.utils.ElParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

/**
 * 处理DoubleCache注解
 *
 * @author Roylic
 * 2022/5/17
 */
@Slf4j
@Aspect
@Component
@AllArgsConstructor
public class CacheAspect {

    private final Cache cache;

    private final RedisTemplate redisTemplate;


    @Pointcut("@annotation(com.kdemo.springcloud.anno.DoubleCache)")
    public void cacheAspect(){}

    @Around("cacheAspect()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();

        // 拼接springEl表达式的map, 并拼接完整key
        String[] parameterNames = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        TreeMap<String, Object> treeMap = new TreeMap<>();
        for (int i = 0; i < parameterNames.length; i++) {
            treeMap.put(parameterNames[i], args[i]);
        }
        DoubleCache annotation = method.getAnnotation(DoubleCache.class);
        String elResult = ElParser.parse(annotation.key(), treeMap);
        String realKey = annotation.cacheName() + "_" + elResult;

        // 根据不同CacheType进行缓存操作
        switch (annotation.type()) {
            // 强制更新
            case PUT: {
                Object obj = joinPoint.proceed();
                redisTemplate.opsForValue().set(realKey, obj, annotation.l2TimeOut(), TimeUnit.SECONDS);
                cache.put(realKey, obj);
                return obj;
            }

            // 强制删除
            case DELETE: {
                redisTemplate.delete(realKey);
                cache.invalidate(realKey);
                return joinPoint.proceed();
            }

            // 双用逻辑
            case FULL: {
                // 1. 先查Caffeine
                Object caffeineCache = cache.getIfPresent(realKey);
                if (Objects.nonNull(caffeineCache)) {
                    log.info("[CacheAspect] >>> get data from caffeine successfully");
                    return caffeineCache;
                }
                // 2. 再查Redis
                Object redisCache = redisTemplate.opsForValue().get(realKey);
                if (Objects.nonNull(redisCache)) {
                    log.info("[CacheAspect] >>> get data from redis successfully");
                    return redisCache;
                }
                // 3. 查DB, 并把结果写入Redis和Caffeine中
                log.info("[CacheAspect] >>> need to get data from database");
                Object dbObj = joinPoint.proceed();
                if (Objects.nonNull(dbObj)) {
                    redisTemplate.opsForValue().set(realKey, dbObj, annotation.l2TimeOut(), TimeUnit.SECONDS);
                    cache.put(realKey, dbObj);
                }
                return dbObj;
            }
        }
        // 不可能走到这里
        return null;
    }


}
