package com.example.BE.cache;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()));

        Map<String,RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("employerJobs", defaultConfig.entryTtl(Duration.ofMinutes(15)));
        cacheConfigs.put("jobApplications", defaultConfig.entryTtl(Duration.ofMinutes(5)));
        cacheConfigs.put("jobDetail", defaultConfig.entryTtl(Duration.ofHours(1)));
        cacheConfigs.put("companiesCache", defaultConfig.entryTtl(Duration.ofHours(12)));

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs) // QUAN TRỌNG: Phải có dòng này để apply Map cấu hình riêng[cite: 2]
                .build();
    }

}
