package com.example.weatherforecast.Bean;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.example.weatherforecast.Converter.DataTypeConverter;

import java.io.Serializable;

@Entity(tableName = "located-room")
public class LocatedCity implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "city_name")
    private String name;

    @ColumnInfo(name = "city_temperature")
    private String temperature;

    @ColumnInfo(name = "city_airQuality")
    private String airQuality;

    @ColumnInfo(name = "city_weather")
    private String weather;

    @ColumnInfo(name = "city_data")
    @TypeConverters(DataTypeConverter.class)
    private Data data;
    @ColumnInfo(name = "city_location_id")
    private String locationId;

    public LocatedCity(String name, String temperature, String airQuality, String weather, Data data, String locationId) {
        this.name = name;
        this.temperature = temperature;
        this.airQuality = airQuality;
        this.weather = weather;
        this.data = data;
        this.locationId = locationId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
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

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    @Override
    public String toString() {
        return "LocatedCity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", temperature='" + temperature + '\'' +
                ", airQuality='" + airQuality + '\'' +
                ", weather='" + weather + '\'' +
                ", locationId='" + locationId + '\'' +
                '}';
    }
}
