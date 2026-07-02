package com.example.BE.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPingRunner implements CommandLineRunner {
    private final StringRedisTemplate redisTemplate;

    @Override
    public void run(String... args) {
        redisTemplate.opsForValue().set("ping", "pong");
        System.out.println("Redis test: " + redisTemplate.opsForValue().get("ping"));
    }
}
