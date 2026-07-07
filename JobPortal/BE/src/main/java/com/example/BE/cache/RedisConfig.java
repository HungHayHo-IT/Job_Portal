package com.example.BE.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisConfig {

        // 1. Tạo một Bean ObjectMapper dùng chung cho cả Template và CacheManager
        @Bean
        public ObjectMapper redisObjectMapper() {
                ObjectMapper objectMapper = new ObjectMapper();

                // Thêm module hỗ trợ Java 8 Date/Time (Instant, LocalDateTime, LocalDate...)
                objectMapper.registerModule(new JavaTimeModule());

                // Chuyển format thời gian thành dạng chuỗi dễ đọc (ISO-8601) thay vì mảng số
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

                // BẮT BUỘC: Thêm thông tin Type (@class) vào JSON để khi lấy từ Redis ra nó
                // biết ép kiểu về Class nào
                objectMapper.activateDefaultTyping(
                                objectMapper.getPolymorphicTypeValidator(),
                                ObjectMapper.DefaultTyping.NON_FINAL);

                return objectMapper;
        }

        @Bean
        public RedisTemplate<String, Object> redisTemplate(
                        RedisConnectionFactory connectionFactory,
                        ObjectMapper redisObjectMapper) { // Inject Bean ObjectMapper vào

                RedisTemplate<String, Object> template = new RedisTemplate<>();
                template.setConnectionFactory(connectionFactory);

                // Sử dụng serializer với custom ObjectMapper
                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(
                                redisObjectMapper);

                template.setKeySerializer(new StringRedisSerializer());
                template.setHashKeySerializer(new StringRedisSerializer());

                template.setValueSerializer(serializer);
                template.setHashValueSerializer(serializer);

                template.afterPropertiesSet();

                return template;
        }

        @Bean
        public RedisCacheManager cacheManager(
                        RedisConnectionFactory connectionFactory,
                        ObjectMapper redisObjectMapper) { // Inject Bean ObjectMapper vào

                // Sử dụng serializer với custom ObjectMapper
                GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(
                                redisObjectMapper);

                RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                                .entryTtl(Duration.ofMinutes(10))
                                .disableCachingNullValues()
                                .serializeValuesWith(RedisSerializationContext.SerializationPair
                                                .fromSerializer(serializer)); // Thay thế dòng cũ bằng serializer mới

                Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
                cacheConfigs.put("employerJobs", defaultConfig.entryTtl(Duration.ofMinutes(15)));
                cacheConfigs.put("jobApplications", defaultConfig.entryTtl(Duration.ofMinutes(5)));
                cacheConfigs.put("jobDetail", defaultConfig.entryTtl(Duration.ofHours(1)));
                cacheConfigs.put("companiesCache", defaultConfig.entryTtl(Duration.ofHours(12)));

                return RedisCacheManager.builder(connectionFactory)
                                .cacheDefaults(defaultConfig)
                                .withInitialCacheConfigurations(cacheConfigs)
                                .build();
        }
}