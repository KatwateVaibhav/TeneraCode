package com.tenera.weatherapp.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Main {

	private double temp;
	private double pressure;

	public Main(double temp, double pressure) {
		this.temp = temp;
		this.pressure = pressure;
	}

	public Main() {
	}

	public double getTemp() {
		return temp;
	}

	public void setTemp(double temp) {
		this.temp = temp;
	}

	public double getPressure() {
		return pressure;
	}

	public void setPressure(double pressure) {
		this.pressure = pressure;
	}

	@Override
	public String toString() {
		return "Main [temp=" + temp + ", pressure=" + pressure + "]";
	}

}
