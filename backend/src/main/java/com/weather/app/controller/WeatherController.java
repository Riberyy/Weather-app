package com.weather.app.controller;

import com.weather.app.model.WeatherResponse;
import com.weather.app.service.WeatherService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/weather")
@CrossOrigin(origins = "*")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    /**
     * GET /weather?city=Mumbai
     * GET /weather?lat=19.07&lon=72.87
     */
    @GetMapping
    public ResponseEntity<?> getWeather(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) Double lat,
            @RequestParam(required = false) Double lon) {

        try {
            WeatherResponse response;

            if (city != null && !city.isBlank()) {
                response = weatherService.getWeatherByCity(city.trim());
            } else if (lat != null && lon != null) {
                response = weatherService.getWeatherByCoordinates(lat, lon);
            } else {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "Provide either 'city' or 'lat' and 'lon' parameters."));
            }

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(404)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        return ResponseEntity.ok(Map.of("status", "UP", "service", "Weather API"));
    }
}
