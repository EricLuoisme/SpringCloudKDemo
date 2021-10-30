package com.kdemo.springcloud.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;

import java.sql.Statement;

/**
 * 实现一个mybatis的插件
 */
@Intercepts(@Signature(type = StatementHandler.class, method = "query", args = {Statement.class, ResultHandler.class}))
@Slf4j
public class DaoInterceptor implements Interceptor {

    @Override
    public Object intercept(Invocation invocation) throws Throwable {

        // before executed the sql
        log.info("\n hahahahaha mybatis interceptor before is invoked \n");
        
        Object proceed = invocation.proceed();

        // after executed the sql
        log.info("\n ooooooooo mybatis interceptor after is invoked \n");

        return proceed;
    }


}
