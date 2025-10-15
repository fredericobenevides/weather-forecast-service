package com.avenuecode.weatherforecast.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record WeatherDataDTO(
    double latitude,
    double longitude,
    String timezone,
    @JsonProperty("timezone_abbreviation") String timezoneAbbreviation,
    CurrentDTO current,
    DailyDTO daily
) {}
