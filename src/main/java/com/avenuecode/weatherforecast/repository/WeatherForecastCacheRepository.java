package com.avenuecode.weatherforecast.repository;

import com.avenuecode.weatherforecast.config.CacheConfig;
import com.avenuecode.weatherforecast.dto.WeatherForecastResponseDTO;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class WeatherForecastCacheRepository {

    private final Cache cache;

    public WeatherForecastCacheRepository(CacheManager cacheManager) {
        this.cache = cacheManager.getCache(CacheConfig.CACHE);
        if (cache == null) {
            throw new IllegalStateException("Cache not found: " + CacheConfig.CACHE);
        }
    }

    public Optional<WeatherForecastResponseDTO> findByZipCode(String zipCode) {
        return Optional.ofNullable(cache.get(zipCode, WeatherForecastResponseDTO.class));
    }

    public void save(String zipCode, WeatherForecastResponseDTO weatherForecast) {
        cache.put(zipCode, weatherForecast);
    }
}
