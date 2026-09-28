package com.chidicivok.civokbank.services.implementations;

import com.chidicivok.civokbank.DTOs.responses.WeatherApiResponse;
import com.chidicivok.civokbank.DTOs.responses.WeatherResponse;
import com.chidicivok.civokbank.exceptions.ExternalApiFailureException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class WeatherService {

    // use RestClient interface for http requests to external api
    private final RestClient restClient;

    /*
     * initialize the class with no args constructor but instantiate the rest client within it
     * Because we already know the value it will have at all times and doesn't require the url to be sent to the class every call
     * */
    public WeatherService() {
        this.restClient = RestClient.create("https://api.open-meteo.com");
    }


    /*
     * Method to make request to api and collect data
     *
     * Open Meteo documentation requires Latitude and Longitude be delivered for weather information
     *
     *
     * use get() of RestClient when to make a Get Request
     * */
    public WeatherApiResponse getWeatherFromOpenMeteo(double latitude, double longitude) {
        try {
            return restClient.get()
                    // build the uri for the request based on the API documentation param
                    .uri(uriBuilder -> uriBuilder
                            // first identify the specific path from the url of open meteo in Api doc
                            .path("/v1/forecast")

                            // query parameters as needed queryParam(x,y)
                            // where x is the API parameter name
                            // and y is the specified parameter value
                            .queryParam("latitude", latitude)
                            .queryParam("longitude", longitude)
                            .queryParam(
                                    "current",
                                    "temperature_2m,weather_code"
                            )
                            .queryParam("timezone", "auto")
                            .queryParam("forecast_days", 1)
                            .build())
                    .retrieve()
                    .body(WeatherApiResponse.class);
        } catch (RestClientException e) {
            throw new ExternalApiFailureException("Unable to retrieve weather information | " + e);
        }
    }


    public WeatherResponse getWeather(double latitude, double longitude) {

        WeatherApiResponse apiResponse = getWeatherFromOpenMeteo(latitude, longitude);

        String condition = getWeatherCondition(apiResponse.current().weatherCode());

        return new WeatherResponse(apiResponse.current().temperature(), condition);
    }

    // private map weather condition response based on api documentation
    private String getWeatherCondition(Integer weatherCode) {

        return switch (weatherCode) {
            case 0 -> "Clear Sky";
            case 1 -> "Mainly Clear";
            case 2 -> "Partly Cloudy";
            case 3 -> "Overcast";
            case 45, 48 -> "Fog";
            case 51, 53, 55 -> "Drizzle";
            case 56, 57 -> "Freezing Drizzle";
            case 61, 63, 65 -> "Rain";
            case 66, 67 -> "Freezing Rain";
            case 80, 81, 82 -> "Rain Showers";
            case 95 -> "Thunderstorm";
            default -> "Unknown weather info";
        };
    }


}



