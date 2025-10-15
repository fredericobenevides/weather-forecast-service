package com.avenuecode.weatherforecast.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record DailyDTO(
    List<String> time,
    @JsonProperty("temperature_2m_max") List<Double> temperatureMax,
    @JsonProperty("temperature_2m_min") List<Double> temperatureMin
) {}
