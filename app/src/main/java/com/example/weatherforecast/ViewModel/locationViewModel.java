package com.example.weatherforecast.ViewModel;

import android.Manifest;
import android.app.Application;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.weatherforecast.location.MyLocationListener;

public class locationViewModel extends AndroidViewModel {
    private static final String TAG = "locationViewModel";
    // 封装定位信息的LiveData
    private final MutableLiveData<BDLocation> bdLocationResponse = new MutableLiveData<>();
    // 封装错误信息的LiveData
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    public LocationClient mLocationClient = null;
    private final MyLocationListener myListener = new MyLocationListener();

    //权限数组
    private final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    //请求权限意图
    private ActivityResultLauncher<String[]> requestPermissionIntent;

    // 暴露定位结果给Activity观察
    public LiveData<BDLocation> getBDLocation() { return bdLocationResponse; }
    // 暴露错误信息给Activity观察
    public LiveData<String> getErrorMessage() { return errorMessage; }

    public locationViewModel(Application application) {
        super(application);
        InitLocation();
    }

    /**
    * 初始化定位
     */
    public void InitLocation() {
        try {
            mLocationClient = new LocationClient(getApplication());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "InitLocation: 初始化定位客户端失败！" + e);
        }
        if (mLocationClient != null) {
            myListener.setCallback(this::handleLocationResult);

            // 注册定位监听
            mLocationClient.registerLocationListener(myListener);
            LocationClientOption option = new LocationClientOption();
            //如果开发者需要获得当前点的地址信息，此处必须为true
            option.setIsNeedAddress(true);
            //可选，设置是否需要最新版本的地址信息。默认不需要，即参数为false
            option.setNeedNewVersionRgc(true);
            //需将配置好的LocationClientOption对象，通过setLocOption方法传递给LocationClient对象使用
            mLocationClient.setLocOption(option);
        }
    }
    // 处理定位结果回调
    private void handleLocationResult(BDLocation location) {
        bdLocationResponse.postValue(location);
    }
    // 检查权限状态
    public boolean checkPermission() {
        return ContextCompat.checkSelfPermission(
                getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED;
    }
    // 启动定位
    public void startLocation() {
        if (mLocationClient != null) {
            mLocationClient.start();
        }
    }
    @Override
    protected void onCleared() {
        if (mLocationClient != null) {
            mLocationClient.stop();
            mLocationClient = null;
        }
        super.onCleared();
    }
}
