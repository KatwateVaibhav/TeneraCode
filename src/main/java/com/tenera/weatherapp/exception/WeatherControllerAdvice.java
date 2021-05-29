package com.tenera.weatherapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class WeatherControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<WeatherError> mapException(ValidationException ex) {
        WeatherError error = new WeatherError(ex.getErrorCode(),ex.getMessage());
        return new ResponseEntity<WeatherError>(error, HttpStatus.BAD_REQUEST);
    }
}
