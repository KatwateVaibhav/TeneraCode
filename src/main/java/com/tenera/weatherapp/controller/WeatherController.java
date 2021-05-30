package com.tenera.weatherapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tenera.weatherapp.dto.WeatherData;
import com.tenera.weatherapp.dto.WeatherHistory;
import com.tenera.weatherapp.service.WeatherService;

/**
 * @author Vaibhav
 *
 */
@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /**
     * @param location
     * @return Temperature, Pressure and Weather Condition of given location.
     */
    @GetMapping("/current")
    public WeatherData queryCurrentWeather(@RequestParam(value = "location") String location) {
        return weatherService.queryCurrentWeather(location);
    }

    /**
     * @param location
     * @return last 5 times history of Temperature, Pressure and Weather Condition of given location. 
     */
    @GetMapping("/history")
    public WeatherHistory queryHistory(@RequestParam(value = "location") String location) {

        // call weather service
        return weatherService.queryHistory(location);
    }
}
