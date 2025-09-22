package com.example.weatherforecast.ViewModel;

import static com.example.weatherforecast.location.WeatherApp.getContext;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.weatherforecast.Bean.*;
import com.example.weatherforecast.icon.DrawableUtils;
import com.example.weatherforecast.Model.weatherModel.*;
import com.example.weatherforecast.Bean.Data.AirQualityBean;
import com.example.weatherforecast.Bean.AirQualityResponse.IndexesBean;
import com.example.weatherforecast.Bean.Data.LivingIndexBean;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class weatherViewModel extends ViewModel {
    private static final String TAG = "weatherViewModel";
    private static final String API_HOST = "kh487rae6k.re.qweatherapi.com";     // 实际API Host
    private static final String API_KEY = "8dc3ea33ad3b43dcb46bcc08b0bb8337";       // 实际API Key
    private static final String HOUR = "24h";
    private static final String DAY = "15d";
    // 添加计数器（确保所有数据请求成功后更新数据）
    private final AtomicInteger completedRequests = new AtomicInteger(0);
    private static final int TOTAL_REQUESTS = 7; // 总请求数量
    private static String LocationId = null;
    private static SearchCityResponse searchCityResponse;
    private static List<SearchCityResponse.LocationBean> cityList;
    // 私有锁对象
    private final Object lock = new Object();

    // 使用LiveData封装响应和错误信息
    // 城市搜索返回数据的LiveData
    private final MutableLiveData<SearchCityResponse> cityResponse = new MutableLiveData<>();
    // 实时天气返回数据的LiveData
    private final MutableLiveData<RealTimeWeatherResponse> nowWeatherResponse = new MutableLiveData<>();
    // 逐小时天气返回数据的liveData
    private final MutableLiveData<HourlyWeatherResponse> hourlyWeatherResponse = new MutableLiveData<>();
    // 每日天气返回数据的LiveData
    private final MutableLiveData<DailyWeatherResponse> dailyWeatherResponse = new MutableLiveData<>();
    // 城市列表
    private final MutableLiveData<List<CityInfo>> searchCityList = new MutableLiveData<>();
    // 错误信息封装的liveData
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // 天气数据封装的LiveData
    private final MutableLiveData<Data> weatherData = new MutableLiveData<>();
    private Data data = new Data();
    private List<HourlyWeatherItem> hourlyWeatherItemList = new ArrayList<>();
    private List<DailyWeatherItem> dailyWeatherItemList = new ArrayList<>();
    private List<CarouselItem> carouselItemList = new ArrayList<>();
    private LiveData<Data> content;

    // 获取城市Id数据的LiveData
    public LiveData<SearchCityResponse> getCityResponse() { return cityResponse; }
    // 获取实时天气的LiveData
    public LiveData<RealTimeWeatherResponse> getNowWeatherResponse() { return nowWeatherResponse; }
    // 获取逐小时天气的LiveData
    public LiveData<HourlyWeatherResponse> getHourlyWeatherResponse() { return hourlyWeatherResponse; }
    // 向View返回封装每日天气的LiveData
    public LiveData<DailyWeatherResponse> getDailyWeatherResponse() { return dailyWeatherResponse; }
    // 返回城市列表
    public LiveData<List<CityInfo>> getCityList() { return searchCityList; }
    // 获取错误信息的LiveData
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    // 获取天气数据的LiveData
    public LiveData<Data> getWeatherData() { return weatherData; }

    // 发起网络请求的方法
    // 获取城市列表
    public void searchCityList(String name) {
        NetworkRequestAPI searchCity = RetrofitClient.getClient("https://" + API_HOST)
                .create(NetworkRequestAPI.class);

        Call<SearchCityResponse> searchCityCall = searchCity.getCityId(API_KEY, name);
        searchCityCall.enqueue(new Callback<SearchCityResponse>() {
            @Override
            public void onResponse(Call<SearchCityResponse> call, Response<SearchCityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    searchCityResponse = response.body();
                    cityList = searchCityResponse.getLocation();

                    List<CityInfo> cityInfoList = new ArrayList<>();
                    for (SearchCityResponse.LocationBean city : cityList) {
                        CityInfo cityInfo = new CityInfo();
                        cityInfo.setName(city.getName());
                        cityInfo.setAdm2(city.getAdm2());
                        cityInfo.setAdm1(city.getAdm1());
                        cityInfo.setCountry(city.getCountry());
                        cityInfo.setLocationID(city.getId());
                        cityInfoList.add(cityInfo);
                    }
                    // 更新城市列表
                    searchCityList.setValue(cityInfoList);

                    Log.d(TAG, "getData: 请求成功 城市列表：" + cityList.toString());
                } else {
                    //errorMessage.setValue("请求失败，状态码: " + response.code());
                    Log.d(TAG, "searchCity: 请求失败，状态码:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<SearchCityResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                Log.d(TAG, "searchCity: 请求失败，状态码:" + t.getMessage());
            }
        });
    }

    // 搜索城市ID
    public void searchCity(String adm1, String adm2, String locationName, double latitude, double longitude) {
        NetworkRequestAPI searchCity = RetrofitClient.getClient("https://" + API_HOST)
                .create(NetworkRequestAPI.class);

        Call<SearchCityResponse> searchCityCall = searchCity.getCityId(API_KEY, locationName);
        searchCityCall.enqueue(new Callback<SearchCityResponse>() {
            @Override
            public void onResponse(Call<SearchCityResponse> call, Response<SearchCityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cityResponse.setValue(response.body());
                    searchCityResponse = cityResponse.getValue();
                    cityList = searchCityResponse.getLocation();
                    for (SearchCityResponse.LocationBean city : cityList) {
                        if (adm1.contains(city.getAdm1()) && adm2.contains(city.getAdm2())) {
                            LocationId = city.getId();
                            break;
                        }
                    }
                    Log.d(TAG, "getData: 请求成功 当前城市Id：" + LocationId);
                    Log.d(TAG, "getData: 请求成功 城市列表：" + cityList.toString());
                    getData(searchCity, LocationId, latitude, longitude);
                } else {
                    //errorMessage.setValue("请求失败，状态码: " + response.code());
                    Log.d(TAG, "searchCity: 请求失败，状态码:" + response.code());
                }
            }

            @Override
            public void onFailure(Call<SearchCityResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                Log.d(TAG, "searchCity: 请求失败，状态码:" + t.getMessage());
            }
        });
    }
    // 获取数据
    public void getData(NetworkRequestAPI APIService, String locationId, double latitude, double longitude) {
        hourlyWeatherItemList.clear();
        dailyWeatherItemList.clear();
        carouselItemList.clear();
        completedRequests.set(0); // 重置计数器

        Call<RealTimeWeatherResponse> getNowWeatherCall = APIService.getNowWeather(API_KEY, locationId);
        getNowWeatherCall.enqueue(new Callback<RealTimeWeatherResponse>() {

            @Override
            public void onResponse(Call<RealTimeWeatherResponse> call, Response<RealTimeWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    RealTimeWeatherResponse weatherResponse = response.body();
                    nowWeatherResponse.setValue(weatherResponse);
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
                    checkAllRequestsCompleted();
                    Log.d(TAG, "getRealTimeWeather: 请求成功 " + response.body().toString());
                    //Log.d(TAG, "onResponse: data具体值：" + data.toString());
                } else {
                    //errorMessage.setValue("请求失败，状态码: " + response.code());
                    Log.d(TAG, "getRealTimeWeather: 请求失败，状态码:" + response.code());
                }
                checkAllRequestsCompleted();
            }

            @Override
            public void onFailure(Call<RealTimeWeatherResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                checkAllRequestsCompleted();
                Log.d(TAG, "getRealTimeWeather: 请求失败，状态码:" + t.getMessage());
            }
        });


        Call<HourlyWeatherResponse> getHourlyWeatherCall = APIService.getHourlyWeather(HOUR,API_KEY,locationId);
        getHourlyWeatherCall.enqueue(new Callback<HourlyWeatherResponse>() {

            @Override
            public void onResponse(Call<HourlyWeatherResponse> call, Response<HourlyWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HourlyWeatherResponse weatherResponse = response.body();
                    hourlyWeatherResponse.setValue(weatherResponse);
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
                checkAllRequestsCompleted();
            }

            @Override
            public void onFailure(Call<HourlyWeatherResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                Log.d(TAG, "getHourlyWeather: 请求失败，状态码:" + t.getMessage());
                //Log.d(TAG, "getHourlyWeather: 请求失败");
                checkAllRequestsCompleted();
            }
        });


        Call<DailyWeatherResponse> getDailyWeatherCall = APIService.getDailyWeather(DAY,API_KEY,locationId);
        getDailyWeatherCall.enqueue(new Callback<DailyWeatherResponse>() {

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<DailyWeatherResponse> call, Response<DailyWeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    DailyWeatherResponse weatherResponse = response.body();
                    dailyWeatherResponse.setValue(weatherResponse);
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
                checkAllRequestsCompleted();
            }

            @Override
            public void onFailure(Call<DailyWeatherResponse> call, Throwable t) {
                //errorMessage.setValue("网络请求失败: " + t.getMessage());
                //Log.d(TAG, "getDailyWeather: 请求失败");
                Log.d(TAG, "getDailyWeather: 请求失败，状态码:" + t.getMessage());
                checkAllRequestsCompleted();
            }
        });

        // 请求空气质量
        Call<AirQualityResponse> getAirQualityCall = APIService.getAirQuality(latitude, longitude, API_KEY);
        getAirQualityCall.enqueue(new Callback<AirQualityResponse>() {
            @Override
            public void onResponse(Call<AirQualityResponse> call, Response<AirQualityResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AirQualityResponse weatherResponse = response.body();
                    AirQualityBean airQuality = new AirQualityBean();
                    IndexesBean index = weatherResponse.getIndexes().get(0);
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
                checkAllRequestsCompleted();
            }

            @Override
            public void onFailure(Call<AirQualityResponse> call, Throwable t) {
                checkAllRequestsCompleted();
                Log.d(TAG, "onFailure: 空气质量数据请求数据失败！状态码:" + t.getMessage());
            }
        });

        // 请求天气指数
        Call<LivingIndexResponse> getLivingIndexCall = APIService.getLivingIndex("1d", 0, API_KEY, LocationId);
        getLivingIndexCall.enqueue(new Callback<LivingIndexResponse>() {
            @Override
            public void onResponse(Call<LivingIndexResponse> call, Response<LivingIndexResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LivingIndexResponse weatherResponse = response.body();
                    // 设置运动指数建议
                    LivingIndexBean livingIndex = new LivingIndexBean();
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
                    List<LivingIndexBean> livingIndexList = new ArrayList<>();
                    for (int i = 0; i < dailyList.size(); i++) {
                        LivingIndexBean livingIndexItem = new LivingIndexBean();
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
                checkAllRequestsCompleted();
            }

            @Override
            public void onFailure(Call<LivingIndexResponse> call, Throwable t) {
                checkAllRequestsCompleted();
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
                    Log.d(TAG, "onResponse: 请求预警信息成功：" + data.getWarningInfoBean().toString());
                }
                checkAllRequestsCompleted();
            }

            @Override
            public void onFailure(Call<WarningInfoResponse> call, Throwable t) {
                checkAllRequestsCompleted();
                Log.d(TAG, "onFailure: 预警信息请求数据失败！状态码:" + t.getMessage());
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
                checkAllRequestsCompleted();
                Log.d(TAG, "onResponse: 请求分钟级降水信息成功：" + data.getMinutelyPrecip());
            }

            @Override
            public void onFailure(Call<MinutelyPrecipResponse> call, Throwable t) {
                checkAllRequestsCompleted();
                Log.d(TAG, "onFailure: 分钟级降水请求数据失败！状态码:" + t.getMessage());
            }
        });
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

    // 检查所有请求是否已完成
    private void checkAllRequestsCompleted() {
        if (completedRequests.incrementAndGet() == TOTAL_REQUESTS) {
            // 所有请求完成，更新LiveData
            weatherData.setValue(data);
            Log.d(TAG, "所有请求已完成，更新天气数据");
        }
    }
    public LiveData<Data> getContent() {
        return content;
    }

    public void setContent(LiveData<Data> content) {
        this.content = content;
    }
}
