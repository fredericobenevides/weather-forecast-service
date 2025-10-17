package com.avenuecode.weatherforecast.weather.service;

import com.avenuecode.weatherforecast.config.WeatherApiProperties;
import com.avenuecode.weatherforecast.weather.dto.CurrentDTO;
import com.avenuecode.weatherforecast.weather.dto.DailyDTO;
import com.avenuecode.weatherforecast.weather.dto.WeatherDataDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClient;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Answers.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class WeatherServiceTest {

    @Mock(answer = RETURNS_DEEP_STUBS)
    private RestClient restClient;

    @Mock(answer = RETURNS_DEEP_STUBS)
    private WeatherApiProperties properties;

    @InjectMocks
    private WeatherService weatherService;

    @Test
    void getWeatherForecast_whenApiReturnsValidResponse_thenSuccessResponse() {
        double latitude = 37.7906691;
        double longitude = -122.4063213;

        WeatherDataDTO expectedWeatherData = new WeatherDataDTO(
                latitude,
                longitude,
                "America/Los_Angeles",
                "GMT-07:00",
                new CurrentDTO(21.1),
                new DailyDTO(
                        java.util.List.of("2025-10-14", "2025-10-15"),
                        java.util.List.of(21.3, 21.0),
                        java.util.List.of(10.0, 10.0)
                )
        );

        when(restClient.get().uri(any(Function.class)).retrieve().body(WeatherDataDTO.class)).thenReturn(expectedWeatherData);

        WeatherDataDTO result = weatherService.getWeatherForecast(latitude, longitude);

        assertNotNull(result);
        assertEquals(expectedWeatherData, result);
    }
}
