package com.example.weatherforecast.Model;

import com.example.weatherforecast.Bean.AirQualityResponse;
import com.example.weatherforecast.Bean.DailyWeatherResponse;
import com.example.weatherforecast.Bean.HourlyWeatherResponse;
import com.example.weatherforecast.Bean.LivingIndexResponse;
import com.example.weatherforecast.Bean.MinutelyPrecipResponse;
import com.example.weatherforecast.Bean.RealTimeWeatherResponse;
import com.example.weatherforecast.Bean.SearchCityResponse;
import com.example.weatherforecast.Bean.WarningInfoResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class weatherModel {
    // 网络请求接口
    public interface NetworkRequestAPI {
        @GET("/geo/v2/city/lookup")
        Call<SearchCityResponse> getCityId(@Query("key") String key, @Query("location") String location);
        @GET("/v7/weather/now")
        Call<RealTimeWeatherResponse> getNowWeather(@Query("key") String key, @Query("location") String location);
        @GET("/v7/weather/{hours}")
        Call<HourlyWeatherResponse> getHourlyWeather(@Path("hours") String hour, @Query("key") String key, @Query("location") String location);
        @GET("/v7/weather/{days}")
        Call<DailyWeatherResponse> getDailyWeather(@Path("days") String day, @Query("key") String key, @Query("location") String location);
        @GET("/airquality/v1/current/{latitude}/{longitude}")
        Call<AirQualityResponse> getAirQuality(@Path("latitude") double latitude, @Path("longitude") double longitude, @Query("key") String key);
        @GET("/v7/indices/{days}")
        Call<LivingIndexResponse> getLivingIndex(@Path("days") String days, @Query("type") int type, @Query("key") String key, @Query("location") String location);
        @GET("/v7/warning/now")
        Call<WarningInfoResponse> getWarningInformation(@Query("key") String key, @Query("location") String location);
        @GET("/v7/minutely/5m")
        Call<MinutelyPrecipResponse> getMinutelyPrecip(@Query("key") String key, @Query("location") String location);
    }

    // 创建Retrofit实例
    public static class RetrofitClient {
        private static Retrofit retrofit = null;
        public static Retrofit getClient(String baseUrl){
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .baseUrl(baseUrl)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();
            }
            return retrofit;
        }
    }
}
