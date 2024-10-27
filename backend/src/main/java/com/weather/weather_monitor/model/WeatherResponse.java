package com.weather.weather_monitor.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherResponse {

    private Main main;
    private Wind wind;

    @JsonProperty("weather")
    private Weather[] weather;

    @JsonProperty("dt")
    private long dt;

    public static class Main {
        @Getter
        private double temp;

        @JsonProperty("feels_like")
        private double feelsLike;

        private int humidity; 

        public double getFeelsLike() {
            return feelsLike;
        }

        public int getHumidity() {
            return humidity;
        }
    }

    public static class Wind {
        @Getter
        private double speed;
    }

    public static class Weather {
        @Getter
        private String main;
    }
}
