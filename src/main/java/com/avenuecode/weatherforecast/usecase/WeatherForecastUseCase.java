package com.avenuecode.weatherforecast.usecase;

import com.avenuecode.weatherforecast.dto.DailyForecastDTO;
import com.avenuecode.weatherforecast.dto.WeatherForecastResponseDTO;
import com.avenuecode.weatherforecast.repository.WeatherForecastCacheRepository;
import com.avenuecode.weatherforecast.weather.dto.WeatherDataDTO;
import com.avenuecode.weatherforecast.weather.service.WeatherService;
import com.avenuecode.weatherforecast.zipcode.service.ZipCodeService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class WeatherForecastUseCase {

    private final WeatherService weatherService;

    private final ZipCodeService zipCodeService;

    private final WeatherForecastCacheRepository weatherForecastCacheRepository;

    public WeatherForecastUseCase(WeatherService weatherService, ZipCodeService zipCodeService,
                                  WeatherForecastCacheRepository weatherForecastCacheRepository) {
        this.weatherService = weatherService;
        this.zipCodeService = zipCodeService;
        this.weatherForecastCacheRepository = weatherForecastCacheRepository;
    }

    public WeatherForecastResponseDTO getWeatherForecast(String zipCode) {
        var weatherForecastCache = weatherForecastCacheRepository.findByZipCode(zipCode);
        if (weatherForecastCache.isPresent()) {
            return weatherForecastCache.get().setLoadedFromCache();
        }

        var zipCodeData = zipCodeService.getZipCodeData(zipCode);
        var weatherData = weatherService.getWeatherForecast(zipCodeData.latitude(), zipCodeData.longitude());

        var weatherForecastResponse = createWeatherForecastResponse(weatherData);
        weatherForecastCacheRepository.save(zipCode, weatherForecastResponse);
        return weatherForecastResponse;
    }

    private WeatherForecastResponseDTO createWeatherForecastResponse(WeatherDataDTO weatherDataDTO) {
        var daily = weatherDataDTO.daily();
        List<DailyForecastDTO> dailyForecasts = new ArrayList<>();

        for (int i = 0; i < daily.time().size(); i++) {
            var time = daily.time().get(i);
            var minTemp = daily.temperatureMin().get(i);
            var maxTemp = daily.temperatureMax().get(i);

            dailyForecasts.add(new DailyForecastDTO(time, minTemp, maxTemp));
        }

        return new WeatherForecastResponseDTO(
                weatherDataDTO.current().temperature(),
                dailyForecasts
        );
    }
}
