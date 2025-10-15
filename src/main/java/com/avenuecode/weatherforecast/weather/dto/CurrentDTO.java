package com.avenuecode.weatherforecast.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CurrentDTO(
    @JsonProperty("temperature_2m") double temperature
) {}
