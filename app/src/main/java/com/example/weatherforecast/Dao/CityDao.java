package com.example.weatherforecast.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.Data;

import java.util.List;

@Dao
public interface CityDao {

    // 插入城市
    @Insert
    void insertCity(City city);

    // 更新城市（通过对象）
    @Update
    int updateCity(City city);

    // 更新城市（通过名称）
    @Query("UPDATE `city_room` SET city_temperature = :temperature, " +
            "city_airQuality = :airQuality, city_weather = :weather, city_data = :data, city_location_id = :LocationId WHERE city_name = :name")
    int updateCity(String name, String temperature, String airQuality, String weather, Data data, String LocationId);

    // 查询所有城市
    @Query("SELECT * FROM `city_room`")
    List<City> getAllCity();

    // 查询所有城市
    @Query("SELECT * FROM `city_room`")
    LiveData<List<City>> getAllCityLiveData();

    // 查询特定城市（通过名称）
    @Query("SELECT * FROM `city_room` WHERE city_name LIKE :name")
    City getCityByName(String name);
    // 查询特定城市（通过名称）
    @Query("SELECT * FROM `city_room` WHERE city_name LIKE :name")
    LiveData<City> getCityLiveDataByName(String name);

    // 删除城市（通过对象）
    @Delete
    void deleteCity(City city);

    // 删除城市（通过名称）
    @Query("DELETE FROM `city_room` WHERE city_name = :name")
    void deleteCityByName(String name);

    // 删除所有城市
    @Query("DELETE FROM `city_room`")
    void deleteAllCity();
}
