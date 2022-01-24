package com.kdemo.springcloud.limiting;

import java.lang.annotation.*;

/**
 * 自定义限流器注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    String name() default "";

    String key() default "";

    String prefix() default "";

    int period();

    int count();

    LimitType limitType() default LimitType.CUSTOMER;
}
