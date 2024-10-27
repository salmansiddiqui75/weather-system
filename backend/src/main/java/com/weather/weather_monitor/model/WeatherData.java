package com.weather.weather_monitor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
public class WeatherData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private double temp;
    private LocalDate date;
    private double feelsLike; 
    private String mainWeather;
    private Long timeStamp;
    private int humidity;
    private double windSpeed;
}
