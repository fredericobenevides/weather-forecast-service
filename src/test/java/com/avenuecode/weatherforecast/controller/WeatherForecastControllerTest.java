package com.avenuecode.weatherforecast.controller;

import com.avenuecode.weatherforecast.weather.dto.CurrentDTO;
import com.avenuecode.weatherforecast.weather.dto.DailyDTO;
import com.avenuecode.weatherforecast.weather.dto.WeatherDataDTO;
import com.avenuecode.weatherforecast.weather.service.WeatherService;
import com.avenuecode.weatherforecast.zipcode.dto.ZipCodeDataDTO;
import com.avenuecode.weatherforecast.zipcode.service.ZipCodeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class WeatherForecastControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WeatherService weatherService;

    @MockBean
    private ZipCodeService zipCodeService;

    @Test
    void getDataByZipCode_whenValidZipCode_thenReturnsForecast() throws Exception {
        ZipCodeDataDTO mockZipCodeData = new ZipCodeDataDTO(37.7906691, -122.4063213);

        WeatherDataDTO mockWeatherData = new WeatherDataDTO(
                37.7906691, -122.4063213,
                "America/Los_Angeles", "GMT-07:00",
                new CurrentDTO(21.1),
                new DailyDTO(
                        java.util.List.of("2025-10-14", "2025-10-15"),
                        java.util.List.of(21.3, 21.0),
                        java.util.List.of(10.0, 10.0)
                )
        );

        when(zipCodeService.getZipCodeData("94108")).thenReturn(mockZipCodeData);
        when(weatherService.getWeatherForecast(37.7906691, -122.4063213)).thenReturn(mockWeatherData);

        mockMvc.perform(get("/api/forecast/94108"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fromCache").value(false))
                .andExpect(jsonPath("$.temperature").value(21.1))
                .andExpect(jsonPath("$.dailyForecast").isArray())
                .andExpect(jsonPath("$.dailyForecast[0].date").value("2025-10-14"))
                .andExpect(jsonPath("$.dailyForecast[0].min").value(10.0))
                .andExpect(jsonPath("$.dailyForecast[0].max").value(21.3))
                .andExpect(jsonPath("$.dailyForecast[1].date").value("2025-10-15"))
                .andExpect(jsonPath("$.dailyForecast[1].min").value(10.0))
                .andExpect(jsonPath("$.dailyForecast[1].max").value(21.0));;
    }

    @Test
    void getDataByZipCode_WhenExternalApiFails_ThenReturnError() throws Exception {
        when(zipCodeService.getZipCodeData("12345")).thenThrow(new RestClientException("Error"));

        mockMvc.perform(get("/api/forecast/12345"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.status").value(502));
    }
}
