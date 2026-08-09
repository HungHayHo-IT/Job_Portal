package com.example.BE.redis.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class RedisConnectionTest {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testRedisConnection() {
        stringRedisTemplate.opsForValue()
                .set("jobportal:health", "UP", Duration.ofMinutes(1));

        String value = stringRedisTemplate.opsForValue().get("jobportal:health");
        assertEquals("UP", value);
    }
}
