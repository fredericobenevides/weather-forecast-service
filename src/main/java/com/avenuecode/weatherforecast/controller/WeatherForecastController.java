package com.avenuecode.weatherforecast.controller;


import com.avenuecode.weatherforecast.dto.WeatherForecastResponseDTO;
import com.avenuecode.weatherforecast.usecase.WeatherForecastUseCase;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/forecast")
public class WeatherForecastController {

    private final WeatherForecastUseCase weatherForecastUseCase;

    public WeatherForecastController(WeatherForecastUseCase weatherForecastUseCase) {
        this.weatherForecastUseCase = weatherForecastUseCase;
    }

    @GetMapping("/{zipcode}")
    public WeatherForecastResponseDTO getDataByZipCode(@PathVariable("zipcode") String zipCode) {
        return weatherForecastUseCase.getWeatherForecast(zipCode);
    }
}
