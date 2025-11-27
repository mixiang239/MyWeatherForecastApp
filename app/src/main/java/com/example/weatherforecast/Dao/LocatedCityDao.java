package com.example.weatherforecast.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.Data;
import com.example.weatherforecast.Bean.LocatedCity;

import java.util.List;

@Dao
public interface LocatedCityDao {
    // 插入城市
    @Insert
    void insertCity(LocatedCity city);

    // 更新城市（通过对象）
    @Update
    int updateCity(LocatedCity city);

    // 更新城市（通过id）
    @Query("UPDATE `located-room` SET city_temperature = :temperature, " +
            "city_airQuality = :airQuality, city_weather = :weather, city_data = :data, city_location_id = :LocationId WHERE id = 1")
    int updateCity(String temperature, String airQuality, String weather, Data data, String LocationId);

    // 查询所有城市
    @Query("SELECT * FROM `located-room`")
    List<LocatedCity> getAllCity();

    // 查询所有城市
    @Query("SELECT * FROM `located-room`")
    LiveData<List<LocatedCity>> getAllCityLiveData();

    // 查询特定城市（通过名称）
    @Query("SELECT * FROM `located-room` WHERE city_name LIKE :name")
    LocatedCity getCityByName(String name);
    // 查询特定城市（通过名称）
    @Query("SELECT * FROM `located-room` WHERE city_name LIKE :name")
    LiveData<LocatedCity> getCityLiveDataByName(String name);

    // 删除城市（通过对象）
    @Delete
    void deleteCity(LocatedCity city);

    // 删除城市（通过名称）
    @Query("DELETE FROM `located-room` WHERE city_name = :name")
    void deleteCityByName(String name);

    // 删除所有城市
    @Query("DELETE FROM `located-room`")
    void deleteAllCity();
}
