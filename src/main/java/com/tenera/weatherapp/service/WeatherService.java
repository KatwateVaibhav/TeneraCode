package com.tenera.weatherapp.service;

import com.tenera.weatherapp.dto.WeatherData;
import com.tenera.weatherapp.dto.WeatherHistory;

/**
 * @author Vaibhav
 *
 */
public interface WeatherService {
	public WeatherData queryCurrentWeather(String location);

	public WeatherHistory queryHistory(String location);

	public boolean validateInput(String location);

	public String processLocation(String location);

}