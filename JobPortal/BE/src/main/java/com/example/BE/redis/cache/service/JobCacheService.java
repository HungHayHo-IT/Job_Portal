package com.example.BE.redis.cache.service;

import com.example.BE.dto.JobDto;

import java.util.Optional;

public interface  JobCacheService {

    Optional<JobDto> getJob(Long jobId);

    void putJob(JobDto job);

    void evictJob(Long jobId);
}
