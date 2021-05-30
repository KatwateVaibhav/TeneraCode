package com.tenera.weatherapp.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.tenera.weatherapp.constants.ApplicationConstants;
import com.tenera.weatherapp.dto.OpenWeatherResponse;
import com.tenera.weatherapp.dto.WeatherCondition;
import com.tenera.weatherapp.dto.WeatherData;
import com.tenera.weatherapp.dto.WeatherHistory;
import com.tenera.weatherapp.exception.ValidationException;
import com.tenera.weatherapp.repository.WeatherRepository;
import com.tenera.weatherapp.service.WeatherService;

/**
 * This class responsible to provide Weather data.
 */
/**
 * @author Vaibhav
 *
 */
@Service
public class WeatherServiceImpl implements WeatherService {

	Logger logger = LoggerFactory.getLogger(WeatherServiceImpl.class);

	@Value("${api.key}")
	private String apiKey;

	@Value("${current.weather.url}")
	private String url;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private WeatherRepository weatherRepository;

	/**
	 * Calls the Open weather API and gets the weather information. Checks of the weather needs an
     * Umbrella or not. Persists the result in the database. returns the response.
	 */
	public WeatherData queryCurrentWeather(String location) {

		logger.debug("ENTRY : queryCurrentWeather {}", location);

		if (!validateInput(location)) {
			throw new ValidationException(ApplicationConstants.CITY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
		WeatherData weatherData = null;
		try {
			// Format of URL is
			// http://api.openweathermap.org/data/2.5/weather?q=Berlin&APPID=726441bd682ecf1be35712107a98cfd0
			ResponseEntity<OpenWeatherResponse> response = restTemplate.getForEntity(url + ApplicationConstants.QUERY_PARAM_WEATHER_CITY + location + ApplicationConstants.QUERY_PARAM_APP_ID + apiKey, OpenWeatherResponse.class);
			double temp = Objects.requireNonNull(response.getBody()).getMain().getTemp();
			double pressure = Objects.requireNonNull(response.getBody()).getMain().getPressure();

			WeatherCondition weatherCondition = Objects.requireNonNull(response.getBody()).getWeather().stream()
						.filter(w -> w.getMain().equals(ApplicationConstants.RAIN)
							|| w.getMain().equals(ApplicationConstants.THUNDERSTORM)
							|| w.getMain().equals(ApplicationConstants.DRIZZLE))
						.findAny().orElse(null);

			boolean umbrella = weatherCondition != null;
			// make sure same city name are all saved as lower case into the database to
			// reduce duplications.
			weatherData = new WeatherData(processLocation(location), temp, pressure, umbrella);
			weatherRepository.save(weatherData);
		} catch (HttpClientErrorException  exception) {
			logger.error(exception.getMessage(), exception);
			String errorMsg = exception.getResponseBodyAsString();
			JSONObject jsonObject = new JSONObject(errorMsg);
			if (exception instanceof HttpClientErrorException.Unauthorized) {
				throw new ValidationException(jsonObject.getString("message"),exception.getStatusCode());
			} else {
				throw new ValidationException(jsonObject.getString("message"), HttpStatus.NOT_FOUND);
			}
		}
		return weatherData;
	}

	/**
	 * Returns Historical data of City which you have already searched for.
	 */

	public WeatherHistory queryHistory(String location) {

		logger.debug("ENTRY : queryHistory {}", location);

		if (!validateInput(location))
			throw new ValidationException(ApplicationConstants.CITY_NOT_FOUND, HttpStatus.NOT_FOUND);
		if (location != null && !location.isEmpty()) {
		List<WeatherData> lastFiveQueries = new ArrayList<>(weatherRepository.findTop5ByLocationOrderByIdDesc(processLocation(location)));
		if (lastFiveQueries.isEmpty()) {
			throw new ValidationException(ApplicationConstants.NO_HISTORY, HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
		}

		// Calculate avg values
		double avgTemp = lastFiveQueries.stream().mapToDouble(WeatherData::getTemp).average().orElse(Double.NaN);
		double avgPressure = lastFiveQueries.stream().mapToDouble(WeatherData::getPressure).average().orElse(Double.NaN);

		// set all above values to WeatherHistory
		return new WeatherHistory(avgTemp, avgPressure, lastFiveQueries);
		}else {
			throw new ValidationException(ApplicationConstants.NO_GEOCODE, HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Validation method to check if Location send by user is only character.
	 */
	public boolean validateInput(String location) {
		// consider location only has letters
		boolean allLetters = location.chars().allMatch(Character::isLetter);
			if (allLetters)
				return true;
			// location consist of city and country
			String[] loc = location.split(",");
			if (loc.length != 2) {
				return false;
			} else {
				return validateInput(loc[0]) && validateInput(loc[1]);
			}
	}

	/**
	 * This Method helps us to find location if User send data in this form.
	 * London,UK then this method will return location as london
	 */
	public String processLocation(String location) {
		String[] loc = location.split(",");
		if (loc.length == 2) {
			return loc[0].toLowerCase();
		} else {
			return location;
		}
	}
}
