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
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import com.baidu.location.BDLocation;
import com.example.weatherforecast.Adapter.CarouselAdapter;
import com.example.weatherforecast.Adapter.CityWeatherPagerAdapter;
import com.example.weatherforecast.Adapter.DailyWeatherAdapter;
import com.example.weatherforecast.Adapter.HourlyWeatherAdapter;
import com.example.weatherforecast.DataBase.CityDataBase;
import com.example.weatherforecast.Fragment.AdviceCardFragment;
import com.example.weatherforecast.Fragment.WeatherCardFragment;
import com.example.weatherforecast.R;
import com.example.weatherforecast.ViewModel.locationViewModel;
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
    private static List<DailyWeatherItem> dailyWeatherItemList = new ArrayList<>();
    private static List<HourlyWeatherItem> hourlyWeatherItemList = new ArrayList<>();


    //权限数组
    private final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
    //请求权限意图
    private ActivityResultLauncher<String[]> requestPermissionIntent;
    private weatherViewModel weatherViewModel;
    private locationViewModel locationVM;

    private ViewPager2 viewPager;
    private LinearLayout indicatorLayout;
    private CarouselAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;
    private int currentPage = 0;
    private static final long AUTO_SCROLL_DELAY = 4500; // 3秒轮播间隔
    private static final int INITIAL_POSITION = 0; // 初始位置

    // 定义防晒等级数组
    private String[] sunProtection = {"无需防晒", "建议防晒", "适度防晒", "高度防晒", "避免外出"};
    private String[] dressAdvice = {"宜穿羽绒服", "宜穿厚外套", "宜穿薄外套", "宜穿长袖", "宜穿轻便衣裤", "宜穿短袖短裤", "宜穿防晒透气衣物"};
    private String[] travelAdvice = {"适宜旅游", "较适宜旅游", "旅游条件一般", "较不适宜旅游", "不适宜旅游"};

    ViewPager2 tabViewPager;
    private CityDataBase cityDataBase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.ime());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 初始化按钮，为所有按钮设置监听器
        InitButton();

        // 若位置信息改变，则自动根据当前地址获取对应Id，成功获取后再获取天气数据
        locate();
    }

    // 初始化按钮
    private void InitButton() {
        // 初始化数据库
        cityDataBase = Room.databaseBuilder(WeatherApp.getContext(), CityDataBase.class, "city-database.db")
                .addMigrations(CityDataBase.MIGRATION_1_2)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {

            @Override
            public void run() {
                List<City> cityList = cityDataBase.cityDao().getAllCity();
                tabViewPager = binding.mainViewpager2;
                CityWeatherPagerAdapter adapter = new CityWeatherPagerAdapter(MainActivity.this, cityList);
                runOnUiThread(() -> {
                    tabViewPager.setAdapter(adapter);
                });
            }
        });
        binding.cityManage.setOnClickListener(this);
    }


    // 处理点击事件
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.city_manage) {
            Intent intent = new Intent(MainActivity.this, CityManage.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startAutoScroll();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopAutoScroll();
    }

    private void startAutoScroll() {
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    private void startCarousel(List<CarouselItem> carouselItemList) {

        // 初始化视图
        viewPager = findViewById(R.id.viewPager);

        // 设置适配器
        adapter = new CarouselAdapter(carouselItemList);
        viewPager.setAdapter(adapter);

        // 设置初始位置（实现无限循环）
        viewPager.setCurrentItem(INITIAL_POSITION, false);
        currentPage = INITIAL_POSITION;

        // 设置页面变化监听器
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                currentPage = position;
                //updateIndicators(position % images.size());
            }
        });

        // 设置自动轮播
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentPage == adapter.getItemCount() - 1) {
                    currentPage = INITIAL_POSITION;
                    viewPager.setCurrentItem(currentPage, false);
                } else {
                    viewPager.setCurrentItem(++currentPage, true);
                }
                handler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }
//    /**
//     * 获取数据
//     */
//    private void getWeatherData(String adm1, String adm2, String locationName, double latitude, double longitude) {
//        weatherViewModel = new ViewModelProvider(this).get(weatherViewModel.class);
//        weatherViewModel.getWeatherData().observe(this, response -> {
//            if (response != null) {
//                binding.temperature.setText(response.getRealTimeTem());
//                StringBuilder temDetails = new StringBuilder();
//                try {
//                    temDetails.append(response.getRealTimeText())
//                            .append(" ")
//                            .append(response.getDailyWeatherItemList().get(0).getMinAndMaxTem())
//                            .append(" ")
//                            .append(response.getAirQuality().getCategory())
//                            .append(" ")
//                            .append(response.getAirQuality().getAqiDisplay());
//                    //Log.d(TAG, "getWeatherData: temDetails值为：" + temDetails.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    //Log.d(TAG, "getWeatherData: 设置实时温度时出错，错误信息：" + e.getMessage());
//                }
//                if (response.getDailyWeatherItemList() != null) {
//                     // 设置实时天气数据详情
//                    binding.temperatureDetails.setText(temDetails.toString());
//                    // 每日天气预报
//                    // 竖向布局管理器
//                    LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this);
//                    // 适配器
//                    DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(response.getDailyWeatherItemList());
//                    binding.dailyRecyclerView.setLayoutManager(verticalLayoutManager);
//                    binding.dailyRecyclerView.setAdapter(dailyWeatherAdapter);
//                }
//                if (response.getHourlyWeatherItemList() != null) {
//                    // 逐小时天气预报
//                    // 横向布局管理器
//                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
//                            this,
//                            LinearLayoutManager.HORIZONTAL,
//                            false
//                    );
//                    // 适配器
//                    HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(response.getHourlyWeatherItemList());
//                    binding.hourlyRecyclerview.setLayoutManager(horizontalLayoutManager);
//                    binding.hourlyRecyclerview.setAdapter(hourlyWeatherAdapter);
//                }
//                if (response.getAirQuality() != null) {
//                    binding.airQualityDetails.setText(response.getAirQuality().getCategory() + " " + response.getAirQuality().getAqiDisplay());
//                }
//                try {
//                    if (response.getSportAdvice() != null) {
//                        binding.airQualityAdvice.setText(response.getSportAdvice());
//                    }
//                } catch (Exception e) {
//                    Log.d(TAG, "getWeatherData: 设置运动指数时出错：" + e.getMessage());
//                }
//
//                if (response.getCarouselItemList() != null && response.getCarouselItemList().size() != 0) {
//                    startCarousel(response.getCarouselItemList());
//                }
//
//                // 设置卡片内容
//                if (response.getFeelsLike() != null) {
//                    cardHandler(R.id.fragment_container2, "体感温度",
//                            response.getFeelsLike() + "°", "", R.drawable.temperature);
//                }
//                if (response.getHumidity() != null) {
//                    cardHandler(R.id.fragment_container3, "湿度",
//                            response.getHumidity(), "%", R.drawable.humidity);
//                }
//                if (response.getWindDir() != null && response.getWindScale() != null) {
//                    cardHandler(R.id.fragment_container4, response.getWindDir(),
//                            response.getWindScale(), "级", R.drawable.wind);
//                }
//                if (response.getPressure() != null) {
//                    cardHandler(R.id.fragment_container5, "气压",
//                            response.getPressure(), "百帕", R.drawable.pressure);
//                }
//                if (response.getVis() != null) {
//                    cardHandler(R.id.fragment_container6, "能见度",
//                            response.getVis(), "千米", R.drawable.visibility);
//                }
//                if (response.getPrecip() != null) {
//                    cardHandler(R.id.fragment_container1, "降水量",
//                            response.getPrecip(), "mm", R.drawable.precipitation);
//                }
//
//                // 设置生活卡片内容
//                if (response.getLivingIndexList() != null && response.getLivingIndexList().size() != 0) {
//                    List<Data.LivingIndexBean> lists = response.getLivingIndexList();
//                    for (int i = 0; i < lists.size(); i++) {
//                        switch (Integer.parseInt(lists.get(i).getType())) {
//                            case 3: // 穿衣指数
//                                adviceCardHandle(R.id.life_advice_fragment_container1, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), dressAdvice[Integer.parseInt(lists.get(i).getLevel()) - 1],
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.coat);
//                                break;
//                            case 16: // 防晒指数
//                                adviceCardHandle(R.id.life_advice_fragment_container2, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), sunProtection[Integer.parseInt(lists.get(i).getLevel()) - 1],
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.umbrella);
//                                break;
//                            case 13: // 化妆指数
//                                adviceCardHandle(R.id.life_advice_fragment_container3, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory(),
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.face);
//                                break;
//                            case 9: // 感冒指数
//                                adviceCardHandle(R.id.life_advice_fragment_container4, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), "感冒" + lists.get(i).getCategory(),
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.cold);
//                                break;
//                            case 2: // 洗车指数
//                                adviceCardHandle(R.id.life_advice_fragment_container5, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "洗车",
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.clean_car);
//                                break;
//                            case 1: // 运动指数
//                                adviceCardHandle(R.id.life_advice_fragment_container6, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "户外运动",
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.sport);
//                                break;
//                            case 15: // 交通指数
//                                adviceCardHandle(R.id.life_advice_fragment_container7, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), "交通" + lists.get(i).getCategory(),
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.car_margin);
//                                break;
//                            case 6: // 旅游指数
//                                adviceCardHandle(R.id.life_advice_fragment_container8, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), travelAdvice[Integer.parseInt(lists.get(i).getLevel()) - 1],
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.travel);
//                                break;
//                            case 4: // 钓鱼指数
//                                adviceCardHandle(R.id.life_advice_fragment_container9, lists.get(i).getName(),
//                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "钓鱼",
//                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.fishing);
//                                break;
//                            default:
//
//                        }
//                    }
//                }
//                Log.d(TAG, "getWeatherData: 获取天气数据成功！");
//            } else {
//                Log.d(TAG, "getWeatherData: 获取天气数据失败！");
//            }
//        });
//        // 发起请求（先获取当前城市对应Id，成功获取Id后再发起请求获取天气数据）
//        weatherViewModel.searchCity(adm1,adm2,locationName, latitude, longitude);
//    }
//    private void adviceCardHandle (int containerId, String name, int type, String category, int level, String text, int iconId) {
//        AdviceCardFragment fragment = (AdviceCardFragment) getSupportFragmentManager()
//                .findFragmentById(containerId);
//
//        if (fragment == null) {
//            // 创建新的Fragment
//            fragment = AdviceCardFragment.newInstance(category,iconId);
//            getSupportFragmentManager().beginTransaction()
//                    .replace(containerId, fragment)
//                    .commit();
//        } else {
//            // 更新已有的Fragment
//            fragment.updateContent(category, iconId);
//        }
//    }
//    private void cardHandler(int containerId, String name, String value, String desc, int iconId) {
//        // 创建或获取Fragment实例
//        WeatherCardFragment fragment = (WeatherCardFragment) getSupportFragmentManager()
//                .findFragmentById(containerId);
//
//        if (fragment == null) {
//            // 创建新的Fragment
//            fragment = WeatherCardFragment.newInstance(
//                    name,
//                    value,
//                    desc,
//                    iconId
//            );
//            getSupportFragmentManager().beginTransaction()
//                    .replace(containerId, fragment)
//                    .commit();
//        } else {
//            // 更新现有的Fragment
//            fragment.updateContent(
//                    name,
//                    value,
//                    desc,
//                    iconId
//            );
//        }
//    }
    // 定位
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
        //binding.tvAddressDetail.setText(addr);//设置文本显示
        //Log.d(TAG, "onReceiveLocation: " + city);
//        runOnUiThread(() -> {
//            // 检查视图绑定和控件是否可用
//            if (binding != null && binding.tvAddressDetail != null) {
//                // 处理可能的空地址
//                String displayAddr = (addr != null && !addr.isEmpty()) ? addr :
//                        "位置: 定位失败！";
//                binding.tvAddressDetail.setText(displayAddr);
//            }
//        });
        Log.d(TAG, "onReceiveLocation: 定位结果: " + bdLocation.toString());
        // 设置标题栏区县信息
        binding.titleLocation.setText(district);
        // 拿到地址信息后获取天气数据
        //getWeatherData(province,city,district, latitude, longitude);
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