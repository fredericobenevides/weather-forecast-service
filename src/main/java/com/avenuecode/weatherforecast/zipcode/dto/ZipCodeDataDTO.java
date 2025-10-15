package com.avenuecode.weatherforecast.zipcode.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ZipCodeDataDTO(
        @JsonProperty("lat") double latitude,
        @JsonProperty("lon") double longitude
) { }
