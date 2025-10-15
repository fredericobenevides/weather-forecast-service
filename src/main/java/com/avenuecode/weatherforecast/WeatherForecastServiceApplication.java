package com.avenuecode.weatherforecast;

import com.avenuecode.weatherforecast.config.WeatherApiProperties;
import com.avenuecode.weatherforecast.config.ZipCodeApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({WeatherApiProperties.class, ZipCodeApiProperties.class})
public class WeatherForecastServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(WeatherForecastServiceApplication.class, args);
	}

}
