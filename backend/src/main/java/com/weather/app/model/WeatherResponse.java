package com.weather.app.model;

public class WeatherResponse {
    private String city;
    private double latitude;
    private double longitude;
    private double temperature;
    private double windspeed;
    private int weatherCode;
    private String weatherCondition;
    private String weatherIcon;
    private int humidity;
    private String timezone;
    private boolean isDay;

    public WeatherResponse() {}

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public double getWindspeed() { return windspeed; }
    public void setWindspeed(double windspeed) { this.windspeed = windspeed; }

    public int getWeatherCode() { return weatherCode; }
    public void setWeatherCode(int weatherCode) { this.weatherCode = weatherCode; }

    public String getWeatherCondition() { return weatherCondition; }
    public void setWeatherCondition(String weatherCondition) { this.weatherCondition = weatherCondition; }

    public String getWeatherIcon() { return weatherIcon; }
    public void setWeatherIcon(String weatherIcon) { this.weatherIcon = weatherIcon; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public String getTimezone() { return timezone; }
    public void setTimezone(String timezone) { this.timezone = timezone; }

    public boolean isDay() { return isDay; }
    public void setIsDay(boolean isDay) { this.isDay = isDay; }
}
