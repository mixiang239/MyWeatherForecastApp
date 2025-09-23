package com.example.weatherforecast.ViewModel;

import static com.example.weatherforecast.location.WeatherApp.getContext;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.room.Room;

import com.example.weatherforecast.Bean.AirQualityResponse;
import com.example.weatherforecast.Bean.CarouselItem;
import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.CityInfo;
import com.example.weatherforecast.Bean.DailyWeatherItem;
import com.example.weatherforecast.Bean.DailyWeatherResponse;
import com.example.weatherforecast.Bean.Data;
import com.example.weatherforecast.Bean.HourlyWeatherItem;
import com.example.weatherforecast.Bean.HourlyWeatherResponse;
import com.example.weatherforecast.Bean.LivingIndexResponse;
import com.example.weatherforecast.Bean.MinutelyPrecipResponse;
import com.example.weatherforecast.Bean.RealTimeWeatherResponse;
import com.example.weatherforecast.Bean.WarningInfoResponse;
import com.example.weatherforecast.DataBase.CityDataBase;
import com.example.weatherforecast.Model.weatherModel;
import com.example.weatherforecast.icon.DrawableUtils;
import com.example.weatherforecast.location.WeatherApp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class searchCityWeatherViewModel extends ViewModel {
    private static final String TAG = "searchCityWeatherViewMo";
    private static final String API_HOST = "kh487rae6k.re.qweatherapi.com";     // 实际API Host
    private static final String API_KEY = "8dc3ea33ad3b43dcb46bcc08b0bb8337";       // 实际API Key
    private static final String HOUR = "24h";
    private static final String DAY = "15d";
    // 添加计数器（确保所有数据请求成功后更新数据）
    private final AtomicInteger completedRequests = new AtomicInteger(0);
    private static final int TOTAL_REQUESTS = 7; // 总请求数量
    private List<HourlyWeatherItem> hourlyWeatherItemList = new ArrayList<>();
    private List<DailyWeatherItem> dailyWeatherItemList = new ArrayList<>();
    private List<CarouselItem> carouselItemList = new ArrayList<>();
    private Data data = new Data();
    private CityDataBase cityDataBase;
    private final Object lock = new Object();

    private final MutableLiveData<List<City>> roomCityList = new MutableLiveData<>();
    public LiveData<List<City>> getRoomCityList() { return roomCityList; }

    public void getData(weatherModel.NetworkRequestAPI APIService, String locationId, double latitude, double longitude, CityInfo cityInfo) {
        hourlyWeatherItemList.clear();
        dailyWeatherItemList.clear();
        carouselItemList.clear();
        completedRequests.set(0); // 重置计数器

        // 初始化数据库
        cityDataBase = Room.databaseBuilder(WeatherApp.getContext(), CityDataBase.class, "city-database.db")
                .addMigrations(CityDataBase.MIGRATION_1_2)
                .build();

        //Log.d(TAG, "getData: " + longitude +","+ latitude);
        Call<RealTimeWeatherResponse> getNowWeatherCall = APIService.getNowWeather(API_KEY, locationId);
        getNowWeatherCall.enqueue(new Callback<RealTimeWeatherResponse>() {

            @Override
            public void onResponse(Call<RealTimeWeatherResponse> call, Response<RealTimeWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RealTimeWeatherResponse weatherResponse = response.body();
                    RealTimeWeatherResponse.nowBean now = weatherResponse.getNow();
                    data.setRealTimeTem(now.getTemp() + "°");
                    data.setRealTimeText(now.getText() + " ");
                    data.setFeelsLike(now.getFeelsLike());
                    data.setHumidity(now.getHumidity());
                    data.setWindDir(now.getWindDir());
                    data.setWindScale(now.getWindScale());
                    data.setPressure(now.getPressure());
                    data.setVis(now.getVis());
                    data.setPrecip(now.getPrecip());
                    //weatherData.setValue(data);
                    checkAllRequestsCompleted(cityInfo);
                    Log.d(TAG, "getRealTimeWeather: 请求成功 " + response.body().toString());
                    //Log.d(TAG, "onResponse: data具体值：" + data.toString());
                } else {
                    //errorMessage.setValue("请求失败，状态码: " + response.code());
                    Log.d(TAG, "getRealTimeWeather: 请求失败，状态码:" + response.code());
                }
                checkAllRequestsCompleted(cityInfo);
            }

            @Override
            public void onFailure(Call<RealTimeWeatherResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                checkAllRequestsCompleted(cityInfo);
                Log.d(TAG, "getRealTimeWeather: 请求失败，状态码:" + t.getMessage());
            }
        });


        Call<HourlyWeatherResponse> getHourlyWeatherCall = APIService.getHourlyWeather(HOUR,API_KEY,locationId);
        getHourlyWeatherCall.enqueue(new Callback<HourlyWeatherResponse>() {

            @Override
            public void onResponse(Call<HourlyWeatherResponse> call, Response<HourlyWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HourlyWeatherResponse weatherResponse = response.body();
                    List<HourlyWeatherResponse.HourlyBean> hourlyBeanList = weatherResponse.getHourly();
                    for (int i = 0; i < hourlyBeanList.size(); i++) {
                        HourlyWeatherItem item = new HourlyWeatherItem(nowTimeParsing(hourlyBeanList.get(i).getFxTime()),
                                DrawableUtils.getDrawableResource(getContext(), hourlyBeanList.get(i).getIcon()), hourlyBeanList.get(i).getTemp() + "°");
                        hourlyWeatherItemList.add(item);
                    }
                    data.setHourlyWeatherItemList(hourlyWeatherItemList);
                    //weatherData.setValue(data);
                    Log.d(TAG, "getHourlyWeather: 请求成功，逐小时天气数据：" + response.body().toString());
                } else {
                    //errorMessage.setValue("请求失败，状态码: " + response.code());
                    Log.d(TAG, "getHourlyWeather: 请求失败，状态码:" + response.code());
                }
                checkAllRequestsCompleted(cityInfo);
            }

            @Override
            public void onFailure(Call<HourlyWeatherResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                Log.d(TAG, "getHourlyWeather: 请求失败，状态码:" + t.getMessage());
                //Log.d(TAG, "getHourlyWeather: 请求失败");
                checkAllRequestsCompleted(cityInfo);
            }
        });


        Call<DailyWeatherResponse> getDailyWeatherCall = APIService.getDailyWeather(DAY,API_KEY,locationId);
        getDailyWeatherCall.enqueue(new Callback<DailyWeatherResponse>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DailyWeatherResponse weatherResponse = response.body();
                    List<DailyWeatherResponse.dailyBean> dailyBeanList = weatherResponse.getDaily();
                    for (int i = 0; i < dailyBeanList.size(); i++) {
                        DailyWeatherItem item  = new DailyWeatherItem(dailyTimeParsing(dailyBeanList.get(i).getFxDate()), DrawableUtils.getDrawableResource(getContext(), dailyBeanList.get(i).getIconDay()),
                                dailyBeanList.get(i).getTempMin() + "° / " + dailyBeanList.get(i).getTempMax() + "°");
                        dailyWeatherItemList.add(item);
                    }
                    data.setDailyWeatherItemList(dailyWeatherItemList);
                    //weatherData.setValue(data);
                    Log.d(TAG, "getDailyWeather: 请求成功，每日天气数据：" + response.body().toString());
                } else {
                    //errorMessage.setValue("请求失败，状态码: " + response.code());
                    Log.d(TAG, "getDailyWeather: 请求失败，状态码:" + response.code());
                }
                checkAllRequestsCompleted(cityInfo);
            }

            @Override
            public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                //Log.d(TAG, "getDailyWeather: 请求失败");
                Log.d(TAG, "getDailyWeather: 请求失败，状态码:" + t.getMessage());
                checkAllRequestsCompleted(cityInfo);
            }
        });

        // 请求空气质量
        Call<AirQualityResponse> getAirQualityCall = APIService.getAirQuality(latitude, longitude, API_KEY);
        getAirQualityCall.enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(Call<AirQualityResponse> call, Response<AirQualityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AirQualityResponse weatherResponse = response.body();
                    Data.AirQualityBean airQuality = new Data.AirQualityBean();
                    AirQualityResponse.IndexesBean index = weatherResponse.getIndexes().get(0);
                    airQuality.setAqiDisplay(index.getAqiDisplay());
                    airQuality.setCategory(index.getCategory());
                    airQuality.setAdvice(index.getHealth().getAdvice().getGeneralPopulation());
                    airQuality.setRed(index.getColor().getRed());
                    airQuality.setGreen(index.getColor().getGreen());
                    airQuality.setBlue(index.getColor().getBlue());
                    airQuality.setAlpha(index.getColor().getAlpha());

                    // 轮播图空气质量与建议
                    CarouselItem item = new CarouselItem();
                    item.setItemType(CarouselItem.AIR_TYPE);
                    item.setTitle("空气" + index.getCategory());
                    item.setDetails(index.getHealth().getAdvice().getGeneralPopulation() + index.getHealth().getAdvice().getSensitivePopulation());
                    synchronized (lock) {
                        carouselItemList.add(item);
                    }
                    data.setCarouselItemList(carouselItemList);
                    data.setAirQuality(airQuality);
                    // 更新暴露给View层的LiveData
                    //weatherData.setValue(data);
                    Log.d(TAG, "onResponse: 请求空气质量数据成功：" + data.toString());
                }
                checkAllRequestsCompleted(cityInfo);
            }

            @Override
            public void onFailure(Call<AirQualityResponse> call, Throwable t) {
                checkAllRequestsCompleted(cityInfo);
                Log.d(TAG, "onFailure: 空气质量数据请求数据失败！状态码:" + t.getMessage());
            }
        });

        // 请求天气指数
        Call<LivingIndexResponse> getLivingIndexCall = APIService.getLivingIndex("1d", 0, API_KEY, locationId);
        getLivingIndexCall.enqueue(new Callback<LivingIndexResponse>() {
            @Override
            public void onResponse(Call<LivingIndexResponse> call, Response<LivingIndexResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LivingIndexResponse weatherResponse = response.body();
                    // 设置运动指数建议
                    Data.LivingIndexBean livingIndex = new Data.LivingIndexBean();
                    livingIndex.setCategory(weatherResponse.getDaily().get(0).getCategory());
                    livingIndex.setText(weatherResponse.getDaily().get(0).getText());
                    data.setSportAdvice(weatherResponse.getDaily().get(0).getText());
                    //data.setLivingIndex(livingIndex);
                    // 设置穿衣指数建议
                    CarouselItem item = new CarouselItem();
                    item.setItemType(CarouselItem.ADVICE_TYPE);
                    item.setTitle(weatherResponse.getDaily().get(2).getName());
                    item.setDetails(weatherResponse.getDaily().get(2).getText());
                    synchronized (lock) {
                        carouselItemList.add(item);
                    }
                    data.setCarouselItemList(carouselItemList);
                    // 设置生活指数卡片建议
                    List<LivingIndexResponse.DailyBean> dailyList = weatherResponse.getDaily();
                    List<Data.LivingIndexBean> livingIndexList = new ArrayList<>();
                    for (int i = 0; i < dailyList.size(); i++) {
                        Data.LivingIndexBean livingIndexItem = new Data.LivingIndexBean();
                        livingIndexItem.setCategory(dailyList.get(i).getCategory());
                        livingIndexItem.setText(dailyList.get(i).getText());
                        livingIndexItem.setName(dailyList.get(i).getName());
                        livingIndexItem.setLevel(dailyList.get(i).getLevel());
                        livingIndexItem.setType(dailyList.get(i).getType());
                        livingIndexList.add(livingIndexItem);
                    }
                    data.setLivingIndexList(livingIndexList);
                    // 更新暴露给View层的LiveData
                    //weatherData.setValue(data);
                    Log.d(TAG, "onResponse: 请求天气指数成功：" + weatherResponse.toString());
                }
                checkAllRequestsCompleted(cityInfo);
            }

            @Override
            public void onFailure(Call<LivingIndexResponse> call, Throwable t) {
                checkAllRequestsCompleted(cityInfo);
                Log.d(TAG, "onFailure: 天气指数请求数据失败！状态码:" + t.getMessage());
            }
        });

        // 请求预警信息
        Call<WarningInfoResponse> getWarningInfoCall = APIService.getWarningInformation(API_KEY, locationId);
        getWarningInfoCall.enqueue(new Callback<WarningInfoResponse>() {
            @Override
            public void onResponse(Call<WarningInfoResponse> call, Response<WarningInfoResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WarningInfoResponse weatherResponse = response.body();
                    List<WarningInfoResponse.WarningBean> warnings =weatherResponse.getWarning();
                    List<Data.WarningInfoBean> warningInfoBeanList = new ArrayList<>();
                    if (warnings != null && warnings.size() != 0) {
                        for (int i = 0; i < warnings.size(); i++) {
                            CarouselItem item = new CarouselItem();
                            item.setTitle(warnings.get(i).getTitle().substring(warnings.get(i).getTitle().indexOf("发布") + 2));
                            item.setDetails(warnings.get(i).getText());
                            item.setItemType(CarouselItem.WARNING_TYPE);
                            synchronized (lock) {
                                carouselItemList.add(item);
                            }
                            Data.WarningInfoBean warningInfo = new Data.WarningInfoBean();
                            warningInfo.setTitle(warnings.get(i).getTitle());
                            warningInfo.setWarningInfoText(warnings.get(i).getText());
                            warningInfoBeanList.add(warningInfo);
                        }
                        data.setCarouselItemList(carouselItemList);
                        data.setWarningInfoBean(warningInfoBeanList);
                        //weatherData.setValue(data);
                    }
                }
                checkAllRequestsCompleted(cityInfo);
                Log.d(TAG, "onResponse: 请求预警信息成功：" + data.getWarningInfoBean());
            }

            @Override
            public void onFailure(Call<WarningInfoResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: 预警信息请求数据失败！状态码:" + t.getMessage());
                checkAllRequestsCompleted(cityInfo);

            }
        });

        // 请求分钟级降水
        Call<MinutelyPrecipResponse> getMinutelyPrecip = APIService.getMinutelyPrecip(API_KEY, longitude +","+ latitude);
        getMinutelyPrecip.enqueue(new Callback<MinutelyPrecipResponse>() {
            @Override
            public void onResponse(Call<MinutelyPrecipResponse> call, Response<MinutelyPrecipResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    CarouselItem item = new CarouselItem();
                    item.setItemType(CarouselItem.PRECIPITATION_TYPE);
                    item.setTitle("降雨提醒");
                    item.setDetails(response.body().getSummary());
                    synchronized (lock) {
                        carouselItemList.add(item);
                    }
                    data.setMinutelyPrecip(response.body().getSummary());
                    data.setCarouselItemList(carouselItemList);
                    //weatherData.setValue(data);
                }

                checkAllRequestsCompleted(cityInfo);
                Log.d(TAG, "onResponse: 请求分钟级降水信息成功：" + data.getMinutelyPrecip());
            }

            @Override
            public void onFailure(Call<MinutelyPrecipResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: 分钟级降水请求数据失败！状态码:" + t.getMessage());
                checkAllRequestsCompleted(cityInfo);

            }
        });
    }

    // 检查所有请求是否已完成
    private void checkAllRequestsCompleted(CityInfo cityInfo) {
        if (completedRequests.incrementAndGet() == TOTAL_REQUESTS) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {

                @Override
                public void run() {
                    //cityDataBase.cityDao().deleteAllCity();
                    // 所有请求完成，更新数据库
                    City city = new City(cityInfo.getName(), data.getRealTimeTem(), "空气" +
                            data.getAirQuality().getCategory() + " " + data.getAirQuality().getAqiDisplay(), data.getRealTimeText(), data);
                    if (cityDataBase.cityDao().updateCity(cityInfo.getName(), data.getRealTimeTem(), "空气" +
                            data.getAirQuality().getCategory() + " " + data.getAirQuality().getAqiDisplay(), data.getRealTimeText(), data) != 0){
                        Log.d(TAG, "run: 该城市已经添加，更新天气数据成功！");
                    } else {
                        cityDataBase.cityDao().insertCity(city);
                        Log.d(TAG, "run: 该城市未添加，成功添加该城市！");
                    }

                    List<City> cityList = cityDataBase.cityDao().getAllCity();
                    for (City city1 : cityList) {
                        Log.d(TAG, "id = " + city1.getId());
                        Log.d(TAG, "name = " + city1.getName());
                        Log.d(TAG, "tem = " + city1.getTemperature());
                        Log.d(TAG, "data = " + city1.getData());
                    }
                    // 更新LiveData
                    roomCityList.postValue(cityDataBase.cityDao().getAllCity());
                    Log.d(TAG, "所有请求已完成，更新数据库");
                }
            });


        }
    }

    private String nowTimeParsing(String ISOTime) {
        try {
            // 1. 创建带时区的格式化器
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mmXXX", Locale.getDefault());

            // 2. 解析字符串（自动识别时区）
            Date date = isoFormat.parse(ISOTime);

            // 3. 创建时间格式化器（使用原始时区）
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.getDefault());
            // 移除 UTC 强制设置 → timeFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            // 4. 提取时间部分
            return timeFormat.format(date); // 返回原始时间 03:00
        } catch (ParseException e) {
            e.printStackTrace();
            return "00:00";
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    private String dailyTimeParsing(String time) {
        try {
            // 自定义输入解析器
            DateTimeFormatter inputParser = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            // 自定义输出格式器
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("M月d日");
            LocalDate date = LocalDate.parse(time, inputParser);
            String resDate = date.format(outputFormatter);
            return  resDate;
        } catch (Exception e) {
            e.printStackTrace();
            return "日期格式解析错误";
        }
    }
}
