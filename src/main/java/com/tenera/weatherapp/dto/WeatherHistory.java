package com.tenera.weatherapp.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherHistory {

    @JsonProperty(value = "avg_temp")
    private double avgTemp;

    @JsonProperty(value = "avg_pressure")
    private double avgPressure;

    @JsonProperty(value = "history")
    private List<WeatherData> history;

    public WeatherHistory(double avgTemp, double avgPressure, List<WeatherData> history) {
        this.avgTemp = avgTemp;
        this.avgPressure = avgPressure;
        this.history = history;
    }

	public WeatherHistory() {
	}

	public double getAvgTemp() {
		return avgTemp;
	}

	public void setAvgTemp(double avgTemp) {
		this.avgTemp = avgTemp;
	}

	public double getAvgPressure() {
		return avgPressure;
	}

	public void setAvgPressure(double avgPressure) {
		this.avgPressure = avgPressure;
	}

	public List<WeatherData> getHistory() {
		return history;
	}

	public void setHistory(List<WeatherData> history) {
		this.history = history;
	}

	@Override
	public String toString() {
		return "WeatherHistory [avgTemp=" + avgTemp + ", avgPressure=" + avgPressure + ", history=" + history + "]";
	}
}
