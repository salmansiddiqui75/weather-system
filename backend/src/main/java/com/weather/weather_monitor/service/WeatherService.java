package com.weather.weather_monitor.service;

import com.weather.weather_monitor.model.DailyWeatherSummary;
import com.weather.weather_monitor.model.WeatherData;
import com.weather.weather_monitor.repository.DailyWeatherSummaryRepository;
import com.weather.weather_monitor.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WeatherService {

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Autowired
    private DailyWeatherSummaryRepository dailyWeatherSummaryRepository;

    @Autowired
    private WeatherApiService weatherApiService;

    public void saveWeatherData(WeatherData weatherData) {
        weatherDataRepository.save(weatherData);
    }

    public WeatherData fetchAndSaveWeatherData(String city) {
        com.weather.weather_monitor.model.WeatherResponse response = weatherApiService.fetchWeatherData(city);
        if (response != null && response.getWeather().length > 0) {
            String mainWeather = response.getWeather()[0].getMain();
            double temp = response.getMain().getTemp();
            double feelsLike = response.getMain().getFeelsLike(); // Use the updated method
            int humidity = response.getMain().getHumidity();
            double windSpeed = response.getWind().getSpeed();

            long dt = response.getDt(); // Unix timestamp


            WeatherData weatherData = new WeatherData();
            weatherData.setCity(city);
            weatherData.setMainWeather(mainWeather);
            weatherData.setTemp(temp);
            weatherData.setFeelsLike(feelsLike);
            weatherData.setTimeStamp(dt); // Set timestamp correctly
            weatherData.setDate(LocalDate.now()); // Assuming you want to set today's date
            weatherData.setHumidity(humidity);
            weatherData.setWindSpeed(windSpeed);

            saveWeatherData(weatherData);
            return weatherData;
        }
        return null;
    }

    public DailyWeatherSummary generateDailySummary(String city) {
        List<WeatherData> dailyData = weatherDataRepository.findByCityAndDate(city, LocalDate.now());

        double sumTemp = 0;
        double maxTemp = Double.MIN_VALUE; // Start with the lowest possible value
        double minTemp = Double.MAX_VALUE; // Start with the highest possible value
        double sumFeelsLike = 0;
        double sumHumidity = 0, sumWindSpeed = 0;

        String dominantWeatherCondition = "";

        for (WeatherData data : dailyData) {
            double temp = data.getTemp();
            double feelsLike = data.getFeelsLike();
            int humidity = data.getHumidity();
            double windSpeed = data.getWindSpeed();
            sumTemp += temp;
            sumFeelsLike += feelsLike;
            sumHumidity += humidity;
            sumWindSpeed += windSpeed;

            // Update maxTemp if the current temperature is greater
            if (temp > maxTemp) {
                maxTemp = temp;
            }

            // Update minTemp if the current temperature is lower
            if (temp < minTemp) {
                minTemp = temp;
            }

            // Logic for dominant weather condition
            if (dominantWeatherCondition.isEmpty() || data.getMainWeather().equals(dominantWeatherCondition)) {
                dominantWeatherCondition = data.getMainWeather();
            }
        }

        int dataCount = dailyData.size();

        if (dataCount == 0) {
            return null;
        }

        DailyWeatherSummary summary = new DailyWeatherSummary();
        summary.setCity(city);
        summary.setAverageTemp(sumTemp / dataCount);
        summary.setMaxTemp(maxTemp);
        summary.setMinTemp(minTemp);
        summary.setDominantWeatherCondition(dominantWeatherCondition);
        summary.setAverageFeelsLike(sumFeelsLike / dataCount);
        summary.setAverageHumidity(sumHumidity / dataCount);
        summary.setAverageWindSpeed(sumWindSpeed / dataCount);


        dailyWeatherSummaryRepository.save(summary);
        return summary;
    }


    public String formatTimestamp(long timestamp) {
        Instant instant = Instant.ofEpochSecond(timestamp);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                .withZone(ZoneId.systemDefault());
        return formatter.format(instant);
    }
}
