package com.tenera.weatherapp.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.tenera.weatherapp.dto.OpenWeatherResponse;
import com.tenera.weatherapp.exception.ValidationException;
import com.tenera.weatherapp.service.WeatherService;
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = DEFINED_PORT)
@ContextConfiguration
class WeatherControllerTest {
	
	
	@Autowired
    private TestRestTemplate restTemplate;
	
	@Autowired
	private WeatherService weatherService;
	
    @LocalServerPort
    private  int port;
    private  String currentWeatherurl;
    private  String historyWeatherurl;
    
    
    
    @BeforeEach
    public void setUp() {
    	currentWeatherurl = "http://localhost:" + port + "/current?location=Berlin";
        historyWeatherurl = "http://localhost:" + port + "/history?location=Berlin";
        
    }
    @Test
   	void wrongApiKey() {
    	ReflectionTestUtils.setField(weatherService, "apiKey", "aezxerr152");
   		ResponseEntity<OpenWeatherResponse> responseEntity = restTemplate.getForEntity(currentWeatherurl,OpenWeatherResponse.class);
   		assertEquals(401, responseEntity.getStatusCodeValue());

   	}

    
    @Test
    void currentWeatherData() {
    	ReflectionTestUtils.setField(weatherService, "apiKey", "726441bd682ecf1be35712107a98cfd0");
        ResponseEntity<OpenWeatherResponse> responseEntity = restTemplate.getForEntity(currentWeatherurl, OpenWeatherResponse.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    
    @Test
     void historyWeatherData() {
        ResponseEntity<OpenWeatherResponse> responseEntity = restTemplate.getForEntity(historyWeatherurl, OpenWeatherResponse.class);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }
    @Test
    public void queryCurrentWeatherWithBadRequest() {
    	ValidationException thrown = assertThrows(ValidationException.class,() -> weatherService.queryCurrentWeather("XYZ"),"No City found");
    	assertEquals(thrown.getErrorCode(),HttpStatus.NOT_FOUND);
    }

    @Test
    public void queryHistoryWithBadRequest() {
        // WHEN AND THEN
    	ValidationException thrown = assertThrows(ValidationException.class,() -> weatherService.queryHistory("111"),"No City found");
    	assertEquals(thrown.getErrorCode(),HttpStatus.NOT_FOUND);
    }

}
