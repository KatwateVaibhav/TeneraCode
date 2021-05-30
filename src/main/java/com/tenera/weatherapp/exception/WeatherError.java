package com.tenera.weatherapp.exception;

import org.springframework.http.HttpStatus;

public class WeatherError {
	private HttpStatus errorCode;
	private String errorMessage;
	public WeatherError(HttpStatus errorCode, String errorMessage) {
		super();
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public HttpStatus getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	
}

