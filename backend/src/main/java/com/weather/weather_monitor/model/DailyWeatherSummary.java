package com.weather.weather_monitor.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class DailyWeatherSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String city;
    private double averageTemp;
    private double maxTemp;
    private double minTemp;
    private String dominantWeatherCondition;
    private double averageFeelsLike;
    private double averageHumidity;    // New field for average humidity
    private double averageWindSpeed;

    // Constructors, getters, and setters
}
