package com.weather.weather_monitor.repository;

import com.weather.weather_monitor.model.DailyWeatherSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyWeatherSummaryRepository extends JpaRepository<DailyWeatherSummary, Long> {
}
