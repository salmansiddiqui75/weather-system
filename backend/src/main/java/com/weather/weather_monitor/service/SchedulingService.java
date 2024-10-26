package com.weather.weather_monitor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SchedulingService {

    @Autowired
    private WeatherService weatherService;

    private final List<String> cities = Arrays.asList("Delhi", "Mumbai", "Chennai", "Bangalore", "Kolkata", "Hyderabad");

    @Scheduled(fixedRate = 100000) // every 5 minutes
    public void fetchWeatherData() {
        for (String city : cities) {
            weatherService.fetchAndSaveWeatherData(city);
        }
    }
}
