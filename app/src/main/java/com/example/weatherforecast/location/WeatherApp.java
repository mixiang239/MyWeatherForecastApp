package com.example.weatherforecast.location;

import android.app.Application;
import android.content.Context;

import com.baidu.location.LocationClient;

public class WeatherApp extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //使用定位需要同意隐私合规政策
        LocationClient.setAgreePrivacy(true);
        context = getApplicationContext();
    }
    // 获取应用程序级的Context
    public static Context getContext() {
        return context;
    }
}
