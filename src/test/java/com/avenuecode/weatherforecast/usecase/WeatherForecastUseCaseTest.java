package com.avenuecode.weatherforecast.usecase;

import com.avenuecode.weatherforecast.dto.DailyForecastDTO;
import com.avenuecode.weatherforecast.dto.WeatherForecastResponseDTO;
import com.avenuecode.weatherforecast.repository.WeatherForecastCacheRepository;
import com.avenuecode.weatherforecast.weather.dto.CurrentDTO;
import com.avenuecode.weatherforecast.weather.dto.DailyDTO;
import com.avenuecode.weatherforecast.weather.dto.WeatherDataDTO;
import com.avenuecode.weatherforecast.weather.service.WeatherService;
import com.avenuecode.weatherforecast.zipcode.dto.ZipCodeDataDTO;
import com.avenuecode.weatherforecast.zipcode.service.ZipCodeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WeatherForecastUseCaseTest {

    @Mock
    private WeatherService weatherService;

    @Mock
    private ZipCodeService zipCodeService;

    @Mock
    private WeatherForecastCacheRepository weatherForecastCacheRepository;

    @InjectMocks
    private WeatherForecastUseCase useCase;

    @Test
    void getWeatherForecast_whenDataNotInCache_thenFetchDataAndAddToCache() {
        // Given
        String zipCode = "94108";
        ZipCodeDataDTO zipCodeData = new ZipCodeDataDTO(37.7749, -122.4194);

        CurrentDTO current = new CurrentDTO(20.0);
        DailyDTO daily = new DailyDTO(
                List.of("2025-10-14", "2025-10-15"),
                List.of(20.0, 21.0),
                List.of(10.0, 11.0)
        );

        WeatherDataDTO weatherData = new WeatherDataDTO(
                zipCodeData.latitude(), zipCodeData.longitude(),
                "America/Los_Angeles", "GMT-7",
                current,
                daily
        );

        when(weatherForecastCacheRepository.findByZipCode(zipCode)).thenReturn(Optional.empty());
        when(zipCodeService.getZipCodeData(zipCode)).thenReturn(zipCodeData);
        when(weatherService.getWeatherForecast(zipCodeData.latitude(), zipCodeData.longitude())).thenReturn(weatherData);

        WeatherForecastResponseDTO result = useCase.getWeatherForecast(zipCode);

        assertNotNull(result);
        assertEquals(20.0, result.getTemperature());
        assertEquals(2, result.getDailyForecast().size());

        DailyForecastDTO dailyResult = result.getDailyForecast().get(0);
        assertEquals("2025-10-14", dailyResult.date());
        assertEquals(10.0, dailyResult.min());
        assertEquals(20.0, dailyResult.max());

        dailyResult = result.getDailyForecast().get(1);
        assertEquals("2025-10-15", dailyResult.date());
        assertEquals(11.0, dailyResult.min());
        assertEquals(21.0, dailyResult.max());

        verify(zipCodeService).getZipCodeData(zipCode);
        verify(weatherService).getWeatherForecast(zipCodeData.latitude(), zipCodeData.longitude());
        verify(weatherForecastCacheRepository).save(eq(zipCode), any(WeatherForecastResponseDTO.class));
    }

    @Test
    void getWeatherForecast_whenDataExistsInCache_thenReturnFromCacheWithFlag() {
        String zipCode = "94108";
        DailyForecastDTO dailyForecast = new DailyForecastDTO("2025-10-14", 10.0, 20.0);
        WeatherForecastResponseDTO expectedWeatherForecastResponse = new WeatherForecastResponseDTO(21.0, List.of(dailyForecast));

        when(weatherForecastCacheRepository.findByZipCode(zipCode)).thenReturn(Optional.of(expectedWeatherForecastResponse));

        WeatherForecastResponseDTO result = useCase.getWeatherForecast(zipCode);

        assertNotNull(result);
        assertEquals(expectedWeatherForecastResponse, result);
        assertTrue(result.isFromCache());

        verify(zipCodeService, times(0)).getZipCodeData(any());
        verify(weatherService, times(0)).getWeatherForecast(anyDouble(), anyDouble());
        verify(weatherForecastCacheRepository, times(0)).save(anyString(), any());
    }
}