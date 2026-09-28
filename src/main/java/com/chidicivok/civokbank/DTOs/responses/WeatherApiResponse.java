package com.chidicivok.civokbank.DTOs.responses;

import com.fasterxml.jackson.annotation.JsonProperty;



public record WeatherApiResponse(CurrentWeather current) {

    // current weather class record
    public record CurrentWeather(
            // get the Json property named temperature_2m and assign as temperature in code
            @JsonProperty("temperature_2m") Double temperature,
            @JsonProperty("weather_code") Integer weatherCode
    ) {


        // record body


    }



}