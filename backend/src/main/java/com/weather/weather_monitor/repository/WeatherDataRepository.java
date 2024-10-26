package com.weather.weather_monitor.repository;

import com.weather.weather_monitor.model.WeatherData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WeatherDataRepository extends JpaRepository<WeatherData, Long> {
    List<WeatherData> findByCityAndDate(String city, LocalDate date);
}
