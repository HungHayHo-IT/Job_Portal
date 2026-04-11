package com.example.BE.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CaffeineCacheConfig {

    @Value("${cache.jobs.ttl-minutes}")
    private int jobsCacheTtlMinutes;

    @Value("${cache.jobs.max-size}")
    private int jobsCacheMaxSize;

    @Value("${cache.companies.ttl-minutes}")
    private int companiesCacheTtlMinutes;

    @Value("${cache.companies.max-size}")
    private int companiesCacheMaxSize;

    @Value("${cache.roles.ttl-days}")
    private int rolesCacheTtlDays;

    @Value("${cache.roles.max-size}")
    private int rolesCacheMaxSize;


    @Bean
    public CacheManager caffeineCacheManager() {

        CaffeineCache jobsCache = new CaffeineCache("jobs",
                Caffeine.newBuilder()
                        .expireAfterWrite(jobsCacheTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(jobsCacheMaxSize)
                        .build());

        CaffeineCache companiesCache = new CaffeineCache("companies",
                Caffeine.newBuilder()
                        .expireAfterWrite(companiesCacheTtlMinutes, TimeUnit.MINUTES)
                        .maximumSize(companiesCacheMaxSize)
                        .build());

        CaffeineCache rolesCache = new CaffeineCache("roles",
                Caffeine.newBuilder()
                        .expireAfterWrite(rolesCacheTtlDays, TimeUnit.DAYS)
                        .maximumSize(rolesCacheMaxSize)
                        .build());

        SimpleCacheManager manager = new SimpleCacheManager();
        manager.setCaches(Arrays.asList(jobsCache, companiesCache, rolesCache));
        return manager;
    }
}
