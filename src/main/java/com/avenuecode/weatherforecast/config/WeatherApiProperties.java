package com.avenuecode.weatherforecast.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "api.weather")
public record WeatherApiProperties(
        List<String> current,
        List<String> daily,
        String forecastDays,
        String temperatureUnit,
        String timezone,
        String url
) {}
