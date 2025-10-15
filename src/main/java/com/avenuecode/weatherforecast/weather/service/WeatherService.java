package com.avenuecode.weatherforecast.weather.service;

import com.avenuecode.weatherforecast.config.WeatherApiProperties;
import com.avenuecode.weatherforecast.weather.dto.WeatherDataDTO;
import com.avenuecode.weatherforecast.zipcode.service.ZipCodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WeatherService {

    private static final Logger log = LoggerFactory.getLogger(ZipCodeService.class);

    private final WeatherApiProperties properties;

    private final RestClient restClient;

    public WeatherService(RestClient weatherRestClient, WeatherApiProperties properties) {
        this.restClient = weatherRestClient;
        this.properties = properties;
    }

    public WeatherDataDTO getWeatherForecast(double latitude, double longitude) {
        log.info("Retrieving weather data: latitude={}, longitude={}", latitude, longitude);

        var currentParams = String.join(",", properties.current());
        var dailyParams = String.join(",", properties.daily());

        var data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("current", currentParams)
                        .queryParam("daily", dailyParams)
                        .queryParam("forecast_days", properties.forecastDays())
                        .queryParam("latitude", latitude)
                        .queryParam("longitude", longitude)
                        .queryParam("temperature_unit", properties.temperatureUnit())
                        .queryParam("timezone", properties.timezone())
                        .build())
                .retrieve()
                .body(WeatherDataDTO.class);

        if (data == null) {
            log.warn("No data found for coordinates: latitude={}, longitude={}", latitude, longitude);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Weather data not found");
        }

        return data;
    }
}
