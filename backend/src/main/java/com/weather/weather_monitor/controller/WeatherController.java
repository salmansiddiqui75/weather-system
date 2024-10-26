package com.weather.weather_monitor.controller;

import com.weather.weather_monitor.model.DailyWeatherSummary;
import com.weather.weather_monitor.model.WeatherData;
import com.weather.weather_monitor.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/weather")
    public WeatherData getWeather(@RequestParam String city) {
        return weatherService.fetchAndSaveWeatherData(city);
    }

    @GetMapping("/daily-summary")
    public DailyWeatherSummary getDailySummary(@RequestParam String city) {
        return weatherService.generateDailySummary(city);
    }
}
