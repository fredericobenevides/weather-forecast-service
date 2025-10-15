package com.avenuecode.weatherforecast.dto;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import java.util.List;

@JsonPropertyOrder({ "fromCache", "temperature", "dailyForecast" })
public class WeatherForecastResponseDTO {
    private boolean fromCache;
    private final double temperature;
    private final List<DailyForecastDTO> dailyForecast;

    public WeatherForecastResponseDTO(double temperature, List<DailyForecastDTO> dailyForecast) {
        this.fromCache = false;
        this.temperature = temperature;
        this.dailyForecast = dailyForecast;
    }

    public WeatherForecastResponseDTO setLoadedFromCache() {
        this.fromCache = true;
        return this;
    }

    public boolean isFromCache() {
        return fromCache;
    }

    public double getTemperature() {
        return temperature;
    }

    public List<DailyForecastDTO> getDailyForecast() {
        return dailyForecast;
    }
}
