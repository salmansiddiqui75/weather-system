import React, { useState } from 'react';
import axios from 'axios';
import './WeatherDashboard.css';

const WeatherDashboard = () => {
  const [city, setCity] = useState('');
  const [weatherData, setWeatherData] = useState(null);
  const [dailySummary, setDailySummary] = useState(null);
  const [threshold] = useState(35); // Example threshold
  const [alert, setAlert] = useState('');
  const [errorMessage, setErrorMessage] = useState(''); // State for error messages
  const [searchedCities, setSearchedCities] = useState(new Set()); // Track searched cities

  // Fetch real-time weather data by city name
  const fetchWeatherData = async (city) => {
    try {
      const response = await axios.get(`/weather?city=${city}`);
      setWeatherData(response.data);
      setErrorMessage(''); // Clear error message if data is fetched successfully

      // Check threshold for alert
      if (response.data.temp > threshold) {
        setAlert(`Alert: Temperature in ${city} exceeds ${threshold}°C!`);
      } else {
        setAlert('');
      }
    } catch (error) {
      console.error('Error fetching weather data', error);
      if (error.response && error.response.status === 404) {
        setErrorMessage(`City "${city}" not found. Please try another city.`);
      } else {
        setErrorMessage('Error fetching weather data. Please try again.');
      }
      setWeatherData(null); // Clear previous weather data on error
    }
  };

  // Fetch daily summary data
  const fetchDailySummary = async (city) => {
    try {
      const response = await axios.get(`/daily-summary?city=${city}`);
      setDailySummary(response.data);
      setErrorMessage(''); // Clear error message if data is fetched successfully
    } catch (error) {
      console.error('Error fetching daily summary', error);
      if (error.response && error.response.status === 404) {
        setErrorMessage(`City "${city}" not found. Please try another city.`);
      } else {
        setErrorMessage('Error fetching daily summary. Please try again.');
      }
      setDailySummary(null); // Clear previous daily summary on error
    }
  };

  // Handle city input change
  const handleCityChange = (e) => {
    setCity(e.target.value);
  };

  // Handle form submission
  const handleSubmit = (e) => {
    e.preventDefault();
    setWeatherData(null); // Clear previous weather data
    setDailySummary(null); // Clear previous daily summary
    setErrorMessage(''); // Clear previous error messages

    // Check if the city has been searched before
    if (!searchedCities.has(city)) {
      setSearchedCities((prev) => new Set(prev).add(city)); // Add city to searched set
      fetchWeatherData(city);
      fetchDailySummary(city);
    } else {
      // If city has been searched before, just fetch again
      fetchWeatherData(city);
      fetchDailySummary(city);
    }
  };

  return (
    <div className="weather-dashboard">
      <h1>Weather Dashboard</h1>
      
      {/* City Search Form */}
      <form onSubmit={handleSubmit}>
        <input
          type="text"
          value={city}
          onChange={handleCityChange}
          placeholder="Enter city name"
          required
        />
        <button type="submit">Get Weather</button>
      </form>

      {/* Display Alerts */}
      {alert && <div className="alert">{alert}</div>}
      
      {/* Display Error Message */}
      {errorMessage && <div className="error">{errorMessage}</div>}

      {/* Info Message for First Search */}
      {!searchedCities.has(city) && city && (
        <div className="info-message">
          Note: Please click the "Get Weather" button twice to fetch the daily summary for this city for the first time.
        </div>
      )}

      {/* Real-time Weather Data */}
      {weatherData && (
        <div className="current-weather">
          <h2>Current Weather in {city}</h2>
          <p>Main: {weatherData.mainWeather || 'Data not available'}</p>
          <div className="temperature">
            <p>Temperature: {weatherData.temp ? `${weatherData.temp.toFixed(2)}°C` : 'N/A'}</p>
            <span className="symbol">🌡️</span> {/* Thermometer symbol */}
          </div>
          <div className="temperature">
            <p>Feels Like: {weatherData.feelsLike ? `${weatherData.feelsLike.toFixed(2)}°C` : 'N/A'}</p>
            <span className="symbol">🌡️</span> {/* Thermometer symbol */}
          </div>
          <p>Humidity: {weatherData.humidity ? `${weatherData.humidity}%` : 'N/A'}</p>
          <p>Wind Speed: {weatherData.windSpeed ? `${weatherData.windSpeed} m/s` : 'N/A'}</p>
          <p>Last Updated: {weatherData.timeStamp ? new Date(weatherData.timeStamp * 1000).toLocaleString() : 'Data not available'}</p>
        </div>
      )}

      {/* Daily Summary */}
      {dailySummary && (
        <div className="daily-summary">
          <h2>Daily Summary for {city}</h2>
          <div className="temp-summary">
            <p>Min Temp: {dailySummary.minTemp ? `${dailySummary.minTemp.toFixed(2)}°C` : 'N/A'} <span className="symbol">⬇️</span></p>
            <p>Max Temp: {dailySummary.maxTemp ? `${dailySummary.maxTemp.toFixed(2)}°C` : 'N/A'} <span className="symbol">⬆️</span></p>
            <p>Avg Temp: {dailySummary.averageTemp ? `${dailySummary.averageTemp.toFixed(2)}°C` : 'N/A'} <span className="symbol">➕</span></p>
          </div>
          <p>Average Feels Like: {dailySummary.averageFeelsLike ? `${dailySummary.averageFeelsLike.toFixed(2)}°C` : 'N/A'}</p>
          <p>Average Humidity: {dailySummary.averageHumidity ? `${dailySummary.averageHumidity}%` : 'N/A'}</p>
          <p>Average Wind Speed: {dailySummary.averageWindSpeed ? `${dailySummary.averageWindSpeed} km/h` : 'N/A'}</p>
          <p>Dominant Condition: {dailySummary.dominantWeatherCondition || 'N/A'}</p>
        </div>
      )}
    </div>
  );
};

export default WeatherDashboard;
