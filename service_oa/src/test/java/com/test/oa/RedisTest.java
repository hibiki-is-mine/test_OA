package com.test.oa;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
public class RedisTest {
    @Autowired
    StringRedisTemplate redisTemplate;
    @Test
    public void test(){
        redisTemplate.opsForValue().set("testk2","testv2");

    }
}
