package com.example.weatherforecast.View;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.LongDef;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import com.baidu.location.BDLocation;
import com.example.weatherforecast.Adapter.CityWeatherPagerAdapter;
import com.example.weatherforecast.DataBase.CityDataBase;
import com.example.weatherforecast.DataBase.LocatedCityDatabase;
import com.example.weatherforecast.Model.weatherModel;
import com.example.weatherforecast.R;
import com.example.weatherforecast.ViewModel.locationViewModel;
import com.example.weatherforecast.ViewModel.searchCityWeatherViewModel;
import com.example.weatherforecast.ViewModel.weatherViewModel;
import com.example.weatherforecast.Bean.*;
import com.example.weatherforecast.databinding.ActivityMainBinding;
import com.example.weatherforecast.location.WeatherApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "MainActivity";
    private ActivityMainBinding binding;
    private static final String API_HOST = "kh487rae6k.re.qweatherapi.com";
    private static final String API_KEY = "8dc3ea33ad3b43dcb46bcc08b0bb8337";

    //权限数组
    private final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    //请求权限意图
    private ActivityResultLauncher<String[]> requestPermissionIntent;
    private weatherViewModel weatherViewModel;
    private searchCityWeatherViewModel searchCityWeatherViewModel;
    private locationViewModel locationVM;

    ViewPager2 tabViewPager;
    private CityDataBase cityDataBase;
    private LocatedCityDatabase locatedCityDatabase;
    private List<City> cityList;
    private LiveData<List<City>> cityListLiveData;
    City located;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        Init();
        // 若位置信息改变，则自动根据当前地址获取对应Id，成功获取后再获取天气数据
        locate();
        InitDB();
//        new Handler().postDelayed(() -> {
//
//        }, 1500); // 延迟1.5秒

        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        tabViewPager = binding.mainViewpager2;
        tabViewPager.setOffscreenPageLimit(20);

        // 加载城市数据并设置适配器
        //loadCities();


        // 设置ViewPager页面变化监听
        tabViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 更新标题栏显示的城市名称
                if (position < cityList.size()) {
                    binding.titleLocation.setText(cityList.get(position).getName());
                }
            }
        });

        // 检查是否有新添加的城市
        checkForNewCity();
    }
    private void checkForNewCity() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("city_added", false)) {
            String cityName = intent.getStringExtra("city_name");
            if (cityName != null) {
                // 使用一个后台线程处理所有操作
                // 延迟执行，确保新城市已成功添加
                new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        safeSwitchCity(cityName);

                    }
                }, 800);

                // 清除intent中的额外数据，避免重复处理

                intent.removeExtra("city_added");
                intent.removeExtra("city_name");
            }
        } else {
            //safeMergeCities();
        }
    }

    // 修改switchToCity，直接在UI线程操作（因为cityList已更新）
    private void switchToCity(String cityName) {
        for (int i = 0; i < cityList.size(); i++) {
            if (cityList.get(i).getName().equals(cityName)) {
                tabViewPager.setCurrentItem(i, true);
                break;
            }
        }
        Toast.makeText(this, "更新成功！", Toast.LENGTH_SHORT).show();
    }

    private void cityManageSwitchToCity() {
        Intent intent = getIntent();
        if (intent != null && intent.getStringExtra("city_name_true") != null) {
            String cityName = intent.getStringExtra("city_name_true");
            switchToCity(cityName);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        checkForNewCity();
    }

    private void Init() {
        // 初始化ViewModel
        weatherViewModel = new ViewModelProvider(this).get(weatherViewModel.class);
        searchCityWeatherViewModel = new ViewModelProvider(this).get(searchCityWeatherViewModel.class);
        binding.cityManage.setOnClickListener(this);
    }
    // 初始化按钮
    private void InitDB() {
        tabViewPager = binding.mainViewpager2;
        // 初始化数据库
        cityDataBase = Room.databaseBuilder(WeatherApp.getContext(), CityDataBase.class, "city-database.db")
                .addMigrations(CityDataBase.MIGRATION_1_2)
                .addMigrations(CityDataBase.MIGRATION_2_3)
                .build();

        locatedCityDatabase = Room.databaseBuilder(WeatherApp.getContext(), LocatedCityDatabase.class, "located-database.db")
                .addMigrations(CityDataBase.MIGRATION_1_2)
                .build();

        weatherModel.NetworkRequestAPI searchCity = weatherModel.RetrofitClient.getClient("https://" + API_HOST)
                .create(weatherModel.NetworkRequestAPI.class);

        safeMergeCities();
    }
    private void safeSwitchCity(String cityName) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // 在后台线程查询两个数据库
                List<LocatedCity> locatedCities = locatedCityDatabase.locatedCityDao().getAllCity();
                List<City> normalCities = cityDataBase.cityDao().getAllCity();

                List<City> mergedList = new ArrayList<>();

                // 添加定位城市（如果有）
                if (locatedCities != null && !locatedCities.isEmpty()) {
                    LocatedCity locatedCity = locatedCities.get(0);
                    City city = new City(locatedCity.getName(), locatedCity.getTemperature(),
                            locatedCity.getAirQuality(), locatedCity.getWeather(), locatedCity.getData());
                    mergedList.add(city);
                }

                // 添加普通城市
                if (normalCities != null) {
                    mergedList.addAll(normalCities);
                }

                // 在主线程更新UI
                runOnUiThread(() -> {
                    cityList = mergedList;
                    CityWeatherPagerAdapter adapter = new CityWeatherPagerAdapter(MainActivity.this, mergedList);
                    binding.mainViewpager2.setAdapter(adapter);

                    switchToCity(cityName);
                });

            } catch (Exception e) {
                Log.e(TAG, "合并城市数据失败", e);
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show());
            }
        });
    }
    private void safeMergeCities() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            try {
                // 在后台线程查询两个数据库
                List<LocatedCity> locatedCities = locatedCityDatabase.locatedCityDao().getAllCity();
                List<City> normalCities = cityDataBase.cityDao().getAllCity();

                List<City> mergedList = new ArrayList<>();

                // 添加定位城市（如果有）
                if (locatedCities != null && !locatedCities.isEmpty()) {
                    LocatedCity locatedCity = locatedCities.get(0);
                    City city = new City(locatedCity.getName(), locatedCity.getTemperature(),
                            locatedCity.getAirQuality(), locatedCity.getWeather(), locatedCity.getData());
                    mergedList.add(city);
                }

                // 添加普通城市
                if (normalCities != null) {
                    mergedList.addAll(normalCities);
                }

                // 在主线程更新UI
                runOnUiThread(() -> {
                    cityList = mergedList;
                    CityWeatherPagerAdapter adapter = new CityWeatherPagerAdapter(MainActivity.this, mergedList);
                    binding.mainViewpager2.setAdapter(adapter);

                    if (!mergedList.isEmpty()) {
                        binding.titleLocation.setText(mergedList.get(0).getName());
                    }
                });

            } catch (Exception e) {
                Log.e(TAG, "合并城市数据失败", e);
                runOnUiThread(() ->
                        Toast.makeText(MainActivity.this, "数据加载失败", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private List<City> getCityList(List<City> dbCityList) {
        List<City> returnCityList = new ArrayList<>();
        locatedCityDatabase.locatedCityDao().getAllCityLiveData().observe(this, new Observer<List<LocatedCity>>() {
            @Override
            public void onChanged(List<LocatedCity> locatedCities) {
                LocatedCity myCity = null;
                // 移除之前的观察者，避免重复调用
                locatedCityDatabase.locatedCityDao().getAllCityLiveData().removeObserver(this);

                if (locatedCities != null && locatedCities.size() != 0) {
                    myCity = locatedCities.get(0);
                }

                if (myCity != null) {
                    City returnMyCity = new City(myCity.getName(), myCity.getTemperature(), myCity.getAirQuality(), myCity.getWeather(), myCity.getData());
                    returnCityList.add(returnMyCity);
                }
            }
        });

        returnCityList.addAll(dbCityList);
        return returnCityList;
    }


    // 处理点击事件
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.city_manage) {
            Intent intent = new Intent(MainActivity.this, CityManage.class);
            Bundle bundle = new Bundle();
            // 添加定位城市信息到Bundle中
            bundle.putSerializable("weather_data", located);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkForNewCity();
        cityManageSwitchToCity();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @SuppressLint("NewApi")
    private void locate() {
        locationVM = new ViewModelProvider(this).get(locationViewModel.class);

        // 观察定位结果
        locationVM.getBDLocation().observe(this, bdLocation -> {
            onBDLocation(bdLocation);
        });
        registerIntent();
        // 检查权限并启动定位
        if (locationVM.checkPermission()) {
            locationVM.startLocation();
        } else {
            requestPermission();
            // 简单延迟执行定位
            new Handler().postDelayed(() -> {
                if (locationVM.checkPermission()) {
                    locationVM.startLocation();
                }
            }, 1000); // 延迟1秒
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onBDLocation(BDLocation bdLocation) {
        double latitude = bdLocation.getLatitude();    //获取纬度信息
        double longitude = bdLocation.getLongitude();    //获取经度信息
        float radius = bdLocation.getRadius();    //获取定位精度，默认值为0.0f
        String coorType = bdLocation.getCoorType();
        //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准
        int errorCode = bdLocation.getLocType();//161  表示网络定位结果
        //获取定位类型、定位错误返回码，具体信息可参照类参考中BDLocation类中的说明
        String addr = bdLocation.getAddrStr();    //获取详细地址信息
        String country = bdLocation.getCountry();    //获取国家
        String province = bdLocation.getProvince();    //获取省份
        String city = bdLocation.getCity();    //获取城市
        String district = bdLocation.getDistrict();    //获取区县
        String street = bdLocation.getStreet();    //获取街道信息
        String locationDescribe = bdLocation.getLocationDescribe();    //获取位置描述信息

        Log.d(TAG, "onReceiveLocation: 定位结果: " + bdLocation.toString());

        weatherViewModel.searchCity(province, city, district, latitude, longitude);
        weatherViewModel.getWeatherData().observe(this, locatedData -> {
            located = new City(district, locatedData.getRealTimeTem(),
                    "空气" + locatedData.getAirQuality().getCategory() + locatedData.getAirQuality().getAqiDisplay(),
                    locatedData.getRealTimeText(), locatedData);

            ExecutorService executor = Executors.newSingleThreadExecutor();
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    if (locatedCityDatabase.locatedCityDao().updateCity(located.getTemperature(), located.getAirQuality(),
                            located.getWeather(), located.getData(), located.getLocationId()) == 0) {
                        LocatedCity myCity = new LocatedCity(located.getName(), located.getTemperature(), located.getAirQuality(), located.getWeather(), located.getData(), located.getLocationId());
                        locatedCityDatabase.locatedCityDao().insertCity(myCity);
                        List<LocatedCity> cities = locatedCityDatabase.locatedCityDao().getAllCity();
                        Log.d(TAG, "定位城市:" + cities.toString());
                    }
                }
            });
            if (located != null) {
                //Log.d(TAG, "当前城市天气请求结果： " + located.toString());
            }
        });
    }
    /**
     * 创建请求权限意图
     */
    private void registerIntent() {
        //请求权限意图
        requestPermissionIntent = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(),
                result -> {
            boolean fineLocation = Boolean.TRUE.equals(result.get(Manifest.permission.ACCESS_FINE_LOCATION));
            if (fineLocation) {
                //权限已经获取到，开始定位
                locationVM.startLocation();
            }
        });
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        requestPermissionIntent.launch(permissions);
    }

}