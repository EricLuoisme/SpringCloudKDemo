package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StructuresTest {

    @Autowired
    private RedisTemplate<String, Object> redisTemplateObj;

    @Test
    public void testForMultiGet() {
        redisTemplateObj.opsForValue().set("Name1", "1");
        redisTemplateObj.opsForValue().set("Name2", "2");
        redisTemplateObj.opsForValue().set("Name3", "4");

        List list = redisTemplateObj.opsForValue().multiGet(Arrays.asList("Name1", "Name2", "Name3"));
        list.forEach(System.out::println);
    }

    @Test
    public void testForIncrOnStr() {
        System.out.println(redisTemplateObj.opsForValue().get("Name1"));
        redisTemplateObj.opsForValue().increment("Name1");
        System.out.println(redisTemplateObj.opsForValue().get("Name1"));
        redisTemplateObj.opsForValue().increment("Name1", 100);
        System.out.println(redisTemplateObj.opsForValue().get("Name1"));
    }

    @Test
    public void testForList() {

        // acting like a queue
        redisTemplateObj.opsForList().leftPush("books", "python");
        redisTemplateObj.opsForList().rightPush("books", "java");
        redisTemplateObj.opsForList().leftPush("books", "golang");
        System.out.println(redisTemplateObj.opsForList().leftPop("books"));
        System.out.println(redisTemplateObj.opsForList().leftPop("books"));
        System.out.println(redisTemplateObj.opsForList().leftPop("books"));
        System.out.println();

        // acting like a stack
        redisTemplateObj.opsForList().leftPush("books", "python");
        redisTemplateObj.opsForList().leftPush("books", "java");
        redisTemplateObj.opsForList().leftPush("books", "golang");
        System.out.println(redisTemplateObj.opsForList().leftPop("books"));
        System.out.println(redisTemplateObj.opsForList().leftPop("books"));
        System.out.println(redisTemplateObj.opsForList().leftPop("books"));
    }

    @Test
    public void testForHash() {
        redisTemplateObj.opsForHash().put("hashBook", "book", "book");
        redisTemplateObj.opsForHash().put("hashBook", "book1", "book1");
        redisTemplateObj.opsForHash().put("hashBook", "book2", "3");
        List<Object> valList = redisTemplateObj.opsForHash()
                .multiGet("hashBook", Arrays.asList("book", "book1", "book2"));
        valList.forEach(System.out::println);

        redisTemplateObj.opsForHash().increment("hashBook", "book2", 2);
        System.out.println(redisTemplateObj.opsForHash().get("hashBook", "book2"));
    }

}
