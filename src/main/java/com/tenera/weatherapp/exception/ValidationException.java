package com.tenera.weatherapp.exception;

import org.springframework.http.HttpStatus;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 4227171665478076830L;
	private String errorMsg;
	private HttpStatus errorCode;

	public ValidationException(String errorMsg, HttpStatus errorCode) {
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public HttpStatus getErrorCode() {
		return errorCode;
	}

}
