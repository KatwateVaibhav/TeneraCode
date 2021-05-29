package com.tenera.weatherapp.exception;

public class ValidationException extends RuntimeException {

	private static final long serialVersionUID = 4227171665478076830L;
	private String errorMsg;
	private int errorCode;

	public ValidationException(String errorMsg, int errorCode) {
		this.errorMsg = errorMsg;
		this.errorCode = errorCode;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public int getErrorCode() {
		return errorCode;
	}

}
