package com.chidicivok.civokbank.controllers;

import com.chidicivok.civokbank.DTOs.responses.WeatherResponse;
import com.chidicivok.civokbank.services.implementations.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weather")
public class WeatherController {

    private final WeatherService weatherService;


    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }


    @GetMapping
    public ResponseEntity<WeatherResponse> getWeather(@RequestParam double latitude, @RequestParam double longitude) {
        return ResponseEntity.ok(weatherService.getWeather(latitude, longitude));
    }


}
