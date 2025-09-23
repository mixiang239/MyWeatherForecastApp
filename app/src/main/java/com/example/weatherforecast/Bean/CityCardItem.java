package com.example.weatherforecast.Bean;

public class CityCardItem {
    private String name;
    private String temp;
    private String airQuality;
    private String weather;

    public CityCardItem() {
    }

    public CityCardItem(String name, String temp, String airQuality, String weather) {
        this.name = name;
        this.temp = temp;
        this.airQuality = airQuality;
        this.weather = weather;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(String airQuality) {
        this.airQuality = airQuality;
    }

    public String getWeather() {
        return weather;
    }

    public void setWeather(String weather) {
        this.weather = weather;
    }

    @Override
    public String toString() {
        return "CityCardItem{" +
                "name='" + name + '\'' +
                ", temp='" + temp + '\'' +
                ", airQuality='" + airQuality + '\'' +
                ", weather='" + weather + '\'' +
                '}';
    }
}
