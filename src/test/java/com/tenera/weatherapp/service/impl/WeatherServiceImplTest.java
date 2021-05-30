package com.tenera.weatherapp.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.web.client.RestTemplate;

import com.tenera.weatherapp.dto.Main;
import com.tenera.weatherapp.dto.OpenWeatherResponse;
import com.tenera.weatherapp.dto.WeatherCondition;
import com.tenera.weatherapp.dto.WeatherData;
import com.tenera.weatherapp.dto.WeatherHistory;
import com.tenera.weatherapp.repository.WeatherRepository;

@SpringBootTest
@ContextConfiguration
class WeatherServiceImplTest {

    @InjectMocks
    private WeatherServiceImpl weatherService;

    @Mock
    WeatherRepository weatherRepository;

    @Mock
    private RestTemplate restTemplate;

    
    @Test
    public void queryCurrentWeatherWithHappyCase() {
        // GIVEN
        Main main = new Main(10.0, 20.0);
        WeatherCondition weatherCondition = new WeatherCondition();
        weatherCondition.setMain("Rain");
        List<WeatherCondition> weather = new ArrayList<>();
        weather.add(weatherCondition);
        OpenWeatherResponse openWeatherResponse = new OpenWeatherResponse(main, weather);
        when(restTemplate.getForEntity(anyString(), any())).thenReturn(new ResponseEntity(openWeatherResponse, HttpStatus.OK));
        when(weatherRepository.save(any())).thenReturn(new WeatherData());

        // WHEN
        WeatherData weatherData = weatherService.queryCurrentWeather("Berlin");

        // THEN
        assertThat(weatherData.getTemp()).isEqualTo(10.0);
        assertThat(weatherData.getPressure()).isEqualTo(20.0);
        assertThat(weatherData.isUmbrella()).isEqualTo(true);
    }


   
    @Test
    public void queryHistoryWithEmptyResponse() {
        // GIVEN
        when(weatherRepository.findTop5ByLocationOrderByIdDesc(anyString())).thenReturn(new ArrayList<>());
        
        // WHEN
        WeatherHistory weatherHistory = weatherService.queryHistory("Berlin");

        // THEN
        assertThat(weatherHistory.getAvgTemp()).isEqualTo(0.0);
        assertThat(weatherHistory.getAvgPressure()).isEqualTo(0.0);
        assertThat(weatherHistory.getHistory().size()).isZero();
    }

    @Test
    public void queryHistoryWithHappyCase() {
        // GIVEN
        WeatherData weatherData1 = new WeatherData("Berlin" , 10.0, 20.0, true);
        WeatherData weatherData2 = new WeatherData();
        weatherData2.setLocation("Berlin");
        weatherData2.setTemp(20.0);
        weatherData2.setPressure(30.0);
        weatherData2.setUmbrella(true);
        
        List<WeatherData> lastQueries = new ArrayList<>();
        lastQueries.add(weatherData1);
        lastQueries.add(weatherData2);
        when(weatherRepository.findTop5ByLocationOrderByIdDesc(anyString())).thenReturn(lastQueries);

        // WHEN
        WeatherHistory weatherHistory = weatherService.queryHistory("Berlin");

        // THEN
        assertThat(weatherHistory.getAvgTemp()).isEqualTo(15.0);
        assertThat(weatherHistory.getAvgPressure()).isEqualTo(25.0);
        assertThat(weatherHistory.getHistory().size()).isEqualTo(2);


    }
}