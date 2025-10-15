package com.avenuecode.weatherforecast.repository;

import com.avenuecode.weatherforecast.config.CacheConfig;
import com.avenuecode.weatherforecast.dto.WeatherForecastResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherForecastCacheRepositoryTest {

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    private WeatherForecastCacheRepository repository;

    @BeforeEach
    void setup() {
        when(cacheManager.getCache(CacheConfig.CACHE)).thenReturn(cache);
        this.repository = new WeatherForecastCacheRepository(cacheManager);
    }

    @Test
    void whenCacheIsNull_thenThrowExceptionWhenConstructingObject() {
        when(cacheManager.getCache(CacheConfig.CACHE)).thenReturn(null);

        assertThrows(IllegalStateException.class, () -> new WeatherForecastCacheRepository(cacheManager));
    }

    @Test
    void findByZipCode_whenValueExistsInCache_ThenReturnValueFromCache() {
        String zipCode = "94108";
        WeatherForecastResponseDTO expectedWeatherForecastResponse = new WeatherForecastResponseDTO(0.0, null);

        when(cache.get(zipCode, WeatherForecastResponseDTO.class)).thenReturn(expectedWeatherForecastResponse);

        Optional<WeatherForecastResponseDTO> result = repository.findByZipCode(zipCode);

        assertTrue(result.isPresent());
        assertEquals(expectedWeatherForecastResponse, result.get());
    }

    @Test
    void findByZipCode_whenValueDoesNotExistInCache_ThenReturnEmptyOptional() {
        String zipCode = "99999";

        when(cache.get(zipCode, WeatherForecastResponseDTO.class)).thenReturn(null);

        Optional<WeatherForecastResponseDTO> result = repository.findByZipCode(zipCode);
        assertTrue(result.isEmpty());
    }

    @Test
    void save_whenCalled_thenAddTheValueInTheCache() {
        String zipCode = "94108";
        WeatherForecastResponseDTO weatherForecastResponseDTO = new WeatherForecastResponseDTO(0.0, null);

        repository.save(zipCode, weatherForecastResponseDTO);
        verify(cache).put(zipCode, weatherForecastResponseDTO);
    }
}
