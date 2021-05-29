package com.tenera.weatherapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherResponse {

    private Main main;

    private List<WeatherCondition> weather;

    public OpenWeatherResponse() {
	}

	public OpenWeatherResponse(Main main, List<WeatherCondition> weather) {
        this.main = main;
        this.weather = weather;
    }

	public Main getMain() {
		return main;
	}

	public void setMain(Main main) {
		this.main = main;
	}

	public List<WeatherCondition> getWeather() {
		return weather;
	}

	public void setWeather(List<WeatherCondition> weather) {
		this.weather = weather;
	}

	@Override
	public String toString() {
		return "OpenWeatherResponse [main=" + main + ", weather=" + weather + "]";
	}
	
}
