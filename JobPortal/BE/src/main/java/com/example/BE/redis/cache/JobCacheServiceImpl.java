package com.example.BE.redis.cache;

import com.example.BE.dto.JobDto;
import com.example.BE.redis.cache.service.JobCacheService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class JobCacheServiceImpl implements JobCacheService {

    private static final String KEY_PREFIX = "jobportal:cache:job:";
    private static final Duration TTL = Duration.ofMinutes(10);

    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<JobDto> getJob(Long jobId) {
        String key = KEY_PREFIX + jobId;
        try {
            String json = stringRedisTemplate.opsForValue().get(key);
            if (json == null) {
                return Optional.empty();
            }
            return Optional.of(objectMapper.readValue(json, JobDto.class));
        } catch (JsonProcessingException e) {
            log.warn("Failed to deserialize cached job {}", jobId, e);
            return Optional.empty();
        }

    }

    @Override
    public void putJob(JobDto job) {
        String key = KEY_PREFIX + job.id();
        try {
            String json = objectMapper.writeValueAsString(job);
            stringRedisTemplate.opsForValue().set(key, json, TTL);
        } catch (JsonProcessingException e) {
            log.warn("Failed to serialize job {} for caching", job.id(), e);
        }
    }

    @Override
    public void evictJob(Long jobId) {
        stringRedisTemplate.delete(KEY_PREFIX + jobId);
    }
}
