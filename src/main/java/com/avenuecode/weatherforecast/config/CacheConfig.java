package com.avenuecode.weatherforecast.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    public final static String CACHE = "weatherForecastCache";

    @Bean
    public Caffeine<Object, Object> caffeine() {
        return Caffeine.newBuilder().expireAfterWrite(1, TimeUnit.MINUTES);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager(CACHE);
        caffeineCacheManager.setCaffeine(caffeine);
        return caffeineCacheManager;
    }
}
