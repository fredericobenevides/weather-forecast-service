package com.avenuecode.weatherforecast.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient weatherRestClient(WeatherApiProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.url())
                .build();
    }

    @Bean
    public RestClient zipCodeRestClient(ZipCodeApiProperties properties) {
        return RestClient.builder()
                .baseUrl(properties.url())
                .build();
    }
}
