package com.kdemo.springcloud.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Redis 相关测试
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class StructuresTest {

    @Autowired
    @Qualifier("normal")
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

    @Test
    public void testForSet() {
        redisTemplateObj.opsForSet().add("setbook", "a", "b", "c");
        Set<Object> setbook = redisTemplateObj.opsForSet().members("setbook");
        setbook.forEach(System.out::println);
        System.out.println();

        redisTemplateObj.opsForSet().add("setbook", "c", "d", "e");
        Set<Object> setbook2 = redisTemplateObj.opsForSet().members("setbook");
        setbook2.forEach(System.out::println);
    }

    @Test
    public void testForZSet() {
        redisTemplateObj.opsForZSet().add("zsetbook", "b", 20);
        redisTemplateObj.opsForZSet().add("zsetbook", "c", 35);

        Set<Object> zsetbook = redisTemplateObj.opsForZSet().rangeByScore("zsetbook", 30, 40);
        zsetbook.forEach(System.out::println);
        System.out.println();

        redisTemplateObj.opsForZSet().incrementScore("zsetbook", "b", 5);
        Set<Object> zsetbook1 = redisTemplateObj.opsForZSet().range("zsetbook", 0, -1);
        zsetbook1.forEach(System.out::println);

        // zrem for faster removing from a zset
        redisTemplateObj.opsForZSet().remove("zsetbook", "b");
        Set<Object> zsetbook2 = redisTemplateObj.opsForZSet().range("zsetbook", 0, -1);
        zsetbook2.forEach(System.out::println);
    }

}
