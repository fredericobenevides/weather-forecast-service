# Weather Forecast Service

The Weather Forecast Service provides real-time and daily weather data based on location

## Building the project

To build the project, run the following command in the terminal:

```sh
./mvnw clean package
```

## Running the application

To run the application, use the following command:

```sh
java -jar target/weather-forecast-service-0.0.1-SNAPSHOT.jar
```

## API

Once the application is running, you can access the weather forecast endpoints:

```bash
curl --request GET --url http://localhost:8080/api/forecast/45268
```

## Third services

The application integrates with the following third-party services:

- [Nominatim](https://nominatim.org/release-docs/latest/api/Overview/): by converting a ZIP code into geographical coordinates (latitude and longitude).
- [OpenMeteo](https://open-meteo.com/): Used to fetch weather forecast data, including current temperature and daily min/max forecasts.

## Configuration

The application can be configured using the `application.yaml` file located in the `src/main/resources` directory. The following properties can be set:

## Design Details

### Requirements

Develop API services capable of retrieving weather forecasts for a specific location. The application should:

1. Accept a zip code from users as input.
2. Provide the current temperature at the requested location as its primary output.
3. Offer additional details, such as the highest and lowest temperatures, and an extended forecast, as bonus features.
4. Implement caching to store forecast details for a duration of 15 minutes for subsequent requests using the same zip code.
5. Display an indicator to notify users if the result is retrieved from the cache.

### Technical Design Decisions

### Caching

I decided to use Caffeine instead of Redis to allow to have a quick simple configuration to avoid to have an external dependency
to build and run the application.
The cons of this decision is that the cache will be lost if the application is restarted
and the cache is not shared between multiple instances of the application.

### Error Handling

The external API returns 200 as response code. I decided for this case to return 404 when there is no data to make clear
for the client there is no data to be returned.

Also, to keep simple the application, I decided to return 502 when there is an error calling the external API.

### Project Structure

I organized the project in a way to keep the code clean and easy to understand. THe packages are organized 
in the following way:

#### Core packages

- `controller`: Contains the REST controllers that handle incoming HTTP requests.
- `usecase`: Contains the business logic which orchestrates the flow of data between the controller and the services.
- `repository`: Contains the classes responsible to handle caching data.
- `dto`: Contains the Data Transfer Objects to be used in the application.
- `adapter`: Contains the ControllerAdvice to handle global exceptions.
- `config`: Contains the configuration classes for the application.
- `model`: Contains the data models used in the application.

#### Weather packages

Packages responsible to collect weather data from external API.

- `service`: Contains the services to call the external API.
- `dto`: Contains the Data Transfer Objects to be used in the weather package.

#### Zipcode packages

Packages responsible to get geographical coordinates based on a zipcode.

- `service`: Contains the services to call the external API.
- `dto`: Contains the Data Transfer Objects to be used in the zipcode package.


### Project Configuration

I decided to include many configuration options in the `application.yaml` file to allow flexibility and easy adjustments 
without changing the code.

### External API Integration

#### Zipcode API (Nominatin)

Nominatim is used to convert a zipcode into geographical coordinates (latitude and longitude).

Nominatim returns a list of location based on a zipcode which allows to return a list of locations for the same zip code.
I decided to return the first location in the list to keep simple the application since I configured the application to use
only one specific country code.

When there is no location found, Nominatim returns an empty list with 200 as response code. I decided to return 404 
to make clear to the client there is no data to be returned.

#### Weather API (Open-Meteo)

Open-Meteo is used to get the weather forecast based on geographical coordinates.

When returning the extended forecast, Open-Meteo returns its data in different arrays, one for each type of data 
(time, temperature_2m_max, temperature_2m_min). I decided to return only the date, min and max temperature for each day to keep simple the application.

When there is no location found, Nominatim returns an empty list with 200 as response code. I decided to return 404
to make clear to the client there is no data to be returned.

### Api Response

The API returns the following JSON response

```json
{
  "fromCache": false,
  "temperature": 52.1,
  "dailyForecast": [
    {
      "date": "2025-10-16",
      "min": 49.4,
      "max": 68.6
    },
    {
      "date": "2025-10-17",
      "min": 44.6,
      "max": 70.9
    },
    {
      "date": "2025-10-18",
      "min": 54.4,
      "max": null
    }
  ]
}
```