package com.avenuecode.weatherforecast.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "api.zipcode")
public record ZipCodeApiProperties(
        String url,
        String format,
        int addressDetails,
        int limit,
        String countryCodes
) {}
