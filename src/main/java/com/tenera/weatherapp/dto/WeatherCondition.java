package com.tenera.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherCondition {

    private String main;
	public WeatherCondition(String main) {
		this.main = main;
	}

	public WeatherCondition() {
	}

	public String getMain() {
		return main;
	}

	public void setMain(String main) {
		this.main = main;
	}

}
