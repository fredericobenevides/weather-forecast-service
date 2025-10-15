package com.avenuecode.weatherforecast.zipcode.service;

import com.avenuecode.weatherforecast.config.ZipCodeApiProperties;
import com.avenuecode.weatherforecast.zipcode.dto.ZipCodeDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ZipCodeService {

    private static final Logger log = LoggerFactory.getLogger(ZipCodeService.class);

    private final ZipCodeApiProperties properties;

    private final RestClient restClient;

    public ZipCodeService(RestClient zipCodeRestClient, ZipCodeApiProperties properties) {
        this.restClient = zipCodeRestClient;
        this.properties = properties;
    }

    public ZipCodeDataDTO getZipCodeData(String zipCode) {
        log.info("Retrieving zip code data: zipCode={}", zipCode);

        var data = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", zipCode)
                        .queryParam("countrycodes", properties.countryCodes())
                        .queryParam("format", properties.format())
                        .queryParam("limit", properties.limit())
                        .build())
                .retrieve()
                .body(ZipCodeDataDTO[].class);

        if (data == null || data.length == 0) {
            log.warn("No data found for Zip Code: zipCode={}", zipCode);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Zip code data not found");
        }

        log.debug("Zip code data retrieved successfully: zipCode={}, data={}", zipCode, data[0]);
        return data[0];
    }
}
