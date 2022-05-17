package com.kdemo.springcloud.utils;

import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.TreeMap;

/**
 * 解析SpringAnnotation中的Erlang内容
 *
 * @author Roylic
 * 2022/5/17
 */
public class ElParser {

    /**
     * 主要支持了在annotation上用 key="order.id", 而入参仅为order的情况
     */
    public static String parse(String elString, TreeMap<String, Object> map) {
        elString = String.format("#{%s}", elString);
        // 创建表达式解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 通过setVariable在上下文中设定变量
        EvaluationContext context = new StandardEvaluationContext();
        map.forEach(context::setVariable);
        // 解析表达式
        Expression expression = parser.parseExpression(elString, new TemplateParserContext());
        // 使用getValue获取表达式值
        return expression.getValue(context, String.class);
    }
}
