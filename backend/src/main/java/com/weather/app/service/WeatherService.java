package com.weather.app.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.weather.app.model.WeatherResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class WeatherService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public WeatherService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
        this.objectMapper = new ObjectMapper();
    }

    public WeatherResponse getWeatherByCity(String city) throws Exception {
        // Step 1: Geocode city name to lat/lon
        String geoUrl = "https://geocoding-api.open-meteo.com/v1/search?name="
                + city + "&count=1&language=en&format=json";

        String geoResponse = webClient.get()
                .uri(geoUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode geoJson = objectMapper.readTree(geoResponse);
        JsonNode results = geoJson.get("results");

        if (results == null || results.isEmpty()) {
            throw new Exception("City not found: " + city);
        }

        JsonNode firstResult = results.get(0);
        double lat = firstResult.get("latitude").asDouble();
        double lon = firstResult.get("longitude").asDouble();
        String cityName = firstResult.get("name").asText();
        String country = firstResult.has("country") ? firstResult.get("country").asText() : "";
        String resolvedCity = cityName + (country.isEmpty() ? "" : ", " + country);

        // Step 2: Fetch weather
        return fetchWeather(lat, lon, resolvedCity);
    }

    public WeatherResponse getWeatherByCoordinates(double lat, double lon) throws Exception {
        // Reverse geocode
        String reverseGeoUrl = "https://nominatim.openstreetmap.org/reverse?lat=" + lat
                + "&lon=" + lon + "&format=json";

        String cityName = "Your Location";
        try {
            String reverseResponse = webClient.get()
                    .uri(reverseGeoUrl)
                    .header("User-Agent", "WeatherApp/1.0")
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            JsonNode reverseJson = objectMapper.readTree(reverseResponse);
            JsonNode address = reverseJson.get("address");
            if (address != null) {
                if (address.has("city")) cityName = address.get("city").asText();
                else if (address.has("town")) cityName = address.get("town").asText();
                else if (address.has("village")) cityName = address.get("village").asText();
                else if (address.has("county")) cityName = address.get("county").asText();

                if (address.has("country")) {
                    cityName += ", " + address.get("country").asText();
                }
            }
        } catch (Exception e) {
            // fallback to coordinates label
            cityName = String.format("%.2f°N, %.2f°E", lat, lon);
        }

        return fetchWeather(lat, lon, cityName);
    }

    private WeatherResponse fetchWeather(double lat, double lon, String cityName) throws Exception {
        String weatherUrl = "https://api.open-meteo.com/v1/forecast"
                + "?latitude=" + lat
                + "&longitude=" + lon
                + "&current_weather=true"
                + "&hourly=relativehumidity_2m"
                + "&timezone=auto";

        String weatherRaw = webClient.get()
                .uri(weatherUrl)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JsonNode weatherJson = objectMapper.readTree(weatherRaw);
        JsonNode current = weatherJson.get("current_weather");

        WeatherResponse response = new WeatherResponse();
        response.setCity(cityName);
        response.setLatitude(lat);
        response.setLongitude(lon);
        response.setTemperature(current.get("temperature").asDouble());
        response.setWindspeed(current.get("windspeed").asDouble());
        response.setWeatherCode(current.get("weathercode").asInt());
        response.setIsDay(current.get("is_day").asInt() == 1);
        response.setTimezone(weatherJson.has("timezone") ? weatherJson.get("timezone").asText() : "UTC");

        // Extract humidity from hourly (first value)
        try {
            JsonNode hourly = weatherJson.get("hourly");
            if (hourly != null && hourly.has("relativehumidity_2m")) {
                JsonNode humArray = hourly.get("relativehumidity_2m");
                if (humArray.size() > 0) {
                    response.setHumidity(humArray.get(0).asInt());
                }
            }
        } catch (Exception ignored) {}

        // Map weather codes
        int code = response.getWeatherCode();
        response.setWeatherCondition(mapWeatherCondition(code));
        response.setWeatherIcon(mapWeatherIcon(code, response.isDay()));

        return response;
    }

    private String mapWeatherCondition(int code) {
        if (code == 0) return "Clear Sky";
        if (code == 1) return "Mainly Clear";
        if (code == 2) return "Partly Cloudy";
        if (code == 3) return "Overcast";
        if (code == 45 || code == 48) return "Foggy";
        if (code >= 51 && code <= 55) return "Drizzle";
        if (code >= 56 && code <= 57) return "Freezing Drizzle";
        if (code >= 61 && code <= 65) return "Rain";
        if (code >= 66 && code <= 67) return "Freezing Rain";
        if (code >= 71 && code <= 77) return "Snow";
        if (code >= 80 && code <= 82) return "Rain Showers";
        if (code == 85 || code == 86) return "Snow Showers";
        if (code == 95) return "Thunderstorm";
        if (code == 96 || code == 99) return "Thunderstorm with Hail";
        return "Unknown";
    }

    private String mapWeatherIcon(int code, boolean isDay) {
        if (code == 0) return isDay ? "☀️" : "🌙";
        if (code == 1 || code == 2) return isDay ? "⛅" : "🌤️";
        if (code == 3) return "☁️";
        if (code == 45 || code == 48) return "🌫️";
        if (code >= 51 && code <= 67) return "🌧️";
        if (code >= 71 && code <= 77) return "❄️";
        if (code >= 80 && code <= 82) return "🌦️";
        if (code == 85 || code == 86) return "🌨️";
        if (code >= 95) return "⛈️";
        return "🌡️";
    }
}
