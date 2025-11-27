package com.example.weatherforecast.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherforecast.Adapter.CarouselAdapter;
import com.example.weatherforecast.Adapter.CityWeatherPagerAdapter;
import com.example.weatherforecast.Adapter.DailyWeatherAdapter;
import com.example.weatherforecast.Adapter.HourlyWeatherAdapter;
import com.example.weatherforecast.Bean.CarouselItem;
import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.Data;
import com.example.weatherforecast.DataBase.CityDataBase;
import com.example.weatherforecast.Model.weatherModel;
import com.example.weatherforecast.R;
import com.example.weatherforecast.View.MainActivity;
import com.example.weatherforecast.ViewModel.searchCityWeatherViewModel;
import com.example.weatherforecast.ViewModel.weatherViewModel;
import com.example.weatherforecast.databinding.AdviceCardFragmentBinding;
import com.example.weatherforecast.databinding.CityWeatherFragmentBinding;
import com.example.weatherforecast.location.WeatherApp;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CityWeatherFragment extends Fragment {
    private CityWeatherFragmentBinding binding;
    private static final String TAG = "CityWeatherFragment";

    // 定义防晒等级数组
    private String[] sunProtection = {"无需防晒", "建议防晒", "适度防晒", "高度防晒", "避免外出"};
    private String[] dressAdvice = {"宜穿羽绒服", "宜穿厚外套", "宜穿薄外套", "宜穿长袖", "宜穿轻便衣裤", "宜穿短袖短裤", "宜穿防晒透气衣物"};
    private String[] travelAdvice = {"适宜旅游", "较适宜旅游", "旅游条件一般", "较不适宜旅游", "不适宜旅游"};

    private ViewPager2 viewPager;
    private LinearLayout indicatorLayout;
    private CarouselAdapter adapter;
    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;
    private int currentPage = 0;
    private static final long AUTO_SCROLL_DELAY = 4500; // 3秒轮播间隔
    private static final int INITIAL_POSITION = 0; // 初始位置

    // 适配器
    DailyWeatherAdapter dailyWeatherAdapter;
    HourlyWeatherAdapter hourlyWeatherAdapter;

    // 添加视图引用缓存
    private SparseArray<View> cardViews = new SparseArray<>();
    private SparseArray<View> adviceCardViews = new SparseArray<>();

    // 添加下拉刷新控件引用
    private SwipeRefreshLayout swipeRefreshLayout;

    private weatherViewModel weatherViewModel;
    private searchCityWeatherViewModel searchCityWeatherViewModel;
    private static final String API_HOST = "kh487rae6k.re.qweatherapi.com";     // 实际API Host
    private Data mdata;
    private CityDataBase cityDataBase;

    // 添加静态方法创建带参数的Fragment实例
    public static CityWeatherFragment newInstance(Data data) {
        CityWeatherFragment fragment = new CityWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable("data_key", data);
        //args.putSerializable("city_list_key", (Serializable) TotalCityList);  // 强制转换为 Serializable
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CityWeatherFragmentBinding.inflate(inflater, container, false);
        //refreshWeatherData();
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            mdata = (Data) getArguments().getSerializable("data_key");
            //updateContent(mdata);
        }

        // 初始化ViewModel
        weatherViewModel = new ViewModelProvider(this).get(weatherViewModel.class);
        searchCityWeatherViewModel = new ViewModelProvider(this).get(searchCityWeatherViewModel.class);

        // 初始化下拉刷新控件
        swipeRefreshLayout = binding.swipeRefreshLayout;

        // 初始化数据库
        cityDataBase = Room.databaseBuilder(WeatherApp.getContext(), CityDataBase.class, "city-database.db")
                .addMigrations(CityDataBase.MIGRATION_1_2)
                .addMigrations(CityDataBase.MIGRATION_2_3)
                .build();

        refreshWeatherData();

        // 设置刷新监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 执行刷新操作
                refreshWeatherData();
                // 显示刷新完成提示
                Toast.makeText(getContext(), "更新成功！", Toast.LENGTH_SHORT).show();
            }
        });

        // 设置刷新动画颜色
        swipeRefreshLayout.setColorSchemeResources(
                R.color.button_pink,
                R.color.blue,
                R.color.gray
        );

        // 初始化适配器（只创建一次）
        dailyWeatherAdapter = new DailyWeatherAdapter(new ArrayList<>());
        hourlyWeatherAdapter = new HourlyWeatherAdapter(new ArrayList<>());

        // 设置RecyclerView（只设置一次）
        binding.dailyRecyclerView.setLayoutManager(new LinearLayoutManager(WeatherApp.getContext()));
        binding.dailyRecyclerView.setAdapter(dailyWeatherAdapter);

        binding.hourlyRecyclerview.setLayoutManager(new LinearLayoutManager(
                WeatherApp.getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.hourlyRecyclerview.setAdapter(hourlyWeatherAdapter);


        // 预缓存卡片视图引用
        cacheCardViews(view);
        cacheAdviceCardViews(view);

        if (getArguments() != null) {
            updateContent((Data) getArguments().getSerializable("data_key"));
        }
    }

    private City newCityWeather;
    private String cityName;

    // 添加刷新天气数据的方法
    private void refreshWeatherData() {
        // 这里实现您的数据刷新逻辑
        // 例如从网络或本地重新获取数据

        weatherModel.NetworkRequestAPI searchCity = weatherModel.RetrofitClient.getClient("https://" + API_HOST)
                .create(weatherModel.NetworkRequestAPI.class);

        Log.d(TAG, "refreshWeatherData: LocationId:" + mdata.getCityName() + " " + mdata.getLocationId() + " " + mdata.getLatitude() + " " + mdata.getLongitude());
        //searchCityWeatherViewModel.getData(searchCity, mdata.getLocationId(), mdata.getLatitude(), mdata.getLongitude(), mdata.getCityName());
        weatherViewModel.getData(searchCity, mdata.getLocationId(), mdata.getLatitude(), mdata.getLongitude(), mdata.getCityName());
        weatherViewModel.getWeatherData().observe(getViewLifecycleOwner(), newWeatherData -> {
            updateContent(newWeatherData);

        });
        cityName = mdata.getCityName();
        // 模拟网络请求延迟
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                // 停止刷新动画
                swipeRefreshLayout.setRefreshing(false);


                // 更新数据库
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    if (cityDataBase.cityDao().updateCity(mdata.getCityName(), mdata.getRealTimeTem(),
                            "空气" + mdata.getAirQuality().getCategory() + " " +mdata.getAirQuality().getAqiDisplay(),
                            mdata.getRealTimeText(), mdata, mdata.getLocationId()) != 0) {
                        Log.d(TAG, "run: 数据库更新成功！");
                    }


                    //newCityWeather = cityDataBase.cityDao().getCityByName(cityName);
                    // 切换到主线程更新UI
//                    if (getActivity() != null) {
//                        getActivity().runOnUiThread(() -> {
//                            updateContent(newCityWeather.getData());
//                        });
//                    }
                });
            }
        }, 300); // 0.3秒延迟模拟网络请求
    }
    @Override
    public void onResume() {
        super.onResume();
        // Fragment可见时启动轮播
        startAutoScroll();

    }

    @Override
    public void onPause() {
        super.onPause();
        // Fragment不可见时停止轮播
        stopAutoScroll();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    // 预缓存天气卡片视图引用
    private void cacheCardViews(View view) {
        int[] cardIds = {R.id.weather_card1, R.id.weather_card2, R.id.weather_card3,
                R.id.weather_card4, R.id.weather_card5, R.id.weather_card6};

        for (int id : cardIds) {
            cardViews.put(id, view.findViewById(id));
        }
    }

    // 预缓存建议卡片视图引用
    private void cacheAdviceCardViews(View view) {
        int[] adviceCardIds = {R.id.advice_card1, R.id.advice_card2, R.id.advice_card3,
                R.id.advice_card4, R.id.advice_card5, R.id.advice_card6,
                R.id.advice_card7, R.id.advice_card8, R.id.advice_card9};

        for (int id : adviceCardIds) {
            adviceCardViews.put(id, view.findViewById(id));
        }
    }
    // 添加更新内容的方法
    public void updateContent(Data data) {

        try {
            // 如果正在刷新，停止刷新动画
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            if (data != null) {
                binding.temperature.setText(data.getRealTimeTem());
                StringBuilder temDetails = new StringBuilder();
                try {
                    temDetails.append(data.getRealTimeText())
                            .append(" ")
                            .append(data.getDailyWeatherItemList().get(0).getMinAndMaxTem())
                            .append(" ")
                            .append(data.getAirQuality().getCategory())
                            .append(" ")
                            .append(data.getAirQuality().getAqiDisplay());
                    //Log.d(TAG, "getWeatherData: temDetails值为：" + temDetails.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.d(TAG, "getWeatherData: 设置实时温度时出错，错误信息：" + e.getMessage());
                }
                if (data.getDailyWeatherItemList() != null) {
                    // 设置实时天气数据详情
                    binding.temperatureDetails.setText(temDetails.toString());
                    // 每日天气预报
                    // 更新适配器中的数据列表
                    dailyWeatherAdapter.updateData(data.getDailyWeatherItemList());
                }
                if (data.getHourlyWeatherItemList() != null) {
                    // 逐小时天气预报
                    // 更新适配器中的数据列表
                    hourlyWeatherAdapter.updateData(data.getHourlyWeatherItemList());
                }
                if (data.getAirQuality() != null) {
                    binding.airQualityDetails.setText(data.getAirQuality().getCategory() + " " + data.getAirQuality().getAqiDisplay());
                }
                try {
                    if (data.getSportAdvice() != null) {
                        binding.airQualityAdvice.setText(data.getSportAdvice());
                    }
                } catch (Exception e) {
                    Log.d(TAG, "getWeatherData: 设置运动指数时出错：" + e.getMessage());
                }

                if (data.getCarouselItemList() != null && data.getCarouselItemList().size() != 0) {
                    startCarousel(data.getCarouselItemList());
                }

                // 设置卡片内容
                if (data.getFeelsLike() != null) {
                    cardHandler(R.id.weather_card2, "体感温度",
                            data.getFeelsLike() + "°", "", R.drawable.temperature);
                }
                if (data.getHumidity() != null) {
                    cardHandler(R.id.weather_card3, "湿度",
                            data.getHumidity(), "%", R.drawable.humidity);
                }
                if (data.getWindDir() != null && data.getWindScale() != null) {
                    cardHandler(R.id.weather_card4, data.getWindDir(),
                            data.getWindScale(), "级", R.drawable.wind);
                }
                if (data.getPressure() != null) {
                    cardHandler(R.id.weather_card5, "气压",
                            data.getPressure(), "百帕", R.drawable.pressure);
                }
                if (data.getVis() != null) {
                    cardHandler(R.id.weather_card6, "能见度",
                            data.getVis(), "千米", R.drawable.visibility);
                }
                if (data.getPrecip() != null) {
                    cardHandler(R.id.weather_card1, "降水量",
                            data.getPrecip(), "mm", R.drawable.precipitation);
                }

                // 设置生活卡片内容
                if (data.getLivingIndexList() != null && data.getLivingIndexList().size() != 0) {
                    List<Data.LivingIndexBean> lists = data.getLivingIndexList();
                    for (int i = 0; i < lists.size(); i++) {
                        switch (Integer.parseInt(lists.get(i).getType())) {
                            case 3: // 穿衣指数
                                adviceCardHandle(R.id.advice_card1, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), dressAdvice[Integer.parseInt(lists.get(i).getLevel()) - 1],
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.coat);
                                break;
                            case 16: // 防晒指数
                                adviceCardHandle(R.id.advice_card2, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), sunProtection[Integer.parseInt(lists.get(i).getLevel()) - 1],
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.umbrella);
                                break;
                            case 13: // 化妆指数
                                adviceCardHandle(R.id.advice_card3, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory(),
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.face);
                                break;
                            case 9: // 感冒指数
                                adviceCardHandle(R.id.advice_card4, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), "感冒" + lists.get(i).getCategory(),
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.cold);
                                break;
                            case 2: // 洗车指数
                                adviceCardHandle(R.id.advice_card5, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "洗车",
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.clean_car);
                                break;
                            case 1: // 运动指数
                                adviceCardHandle(R.id.advice_card6, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "户外运动",
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.sport);
                                break;
                            case 15: // 交通指数
                                adviceCardHandle(R.id.advice_card7, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), "交通" + lists.get(i).getCategory(),
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.car_margin);
                                break;
                            case 6: // 旅游指数
                                adviceCardHandle(R.id.advice_card8, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), travelAdvice[Integer.parseInt(lists.get(i).getLevel()) - 1],
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.travel);
                                break;
                            case 4: // 钓鱼指数
                                adviceCardHandle(R.id.advice_card9, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "钓鱼",
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.fishing);
                                break;
                            default:

                        }
                    }
                }
                Log.d(TAG, "getWeatherData: 获取天气数据成功！");
            }
        } catch (Exception e) {
            // 出现错误时也停止刷新
            if (swipeRefreshLayout != null && swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            throw new RuntimeException(e);
        }

    }

    private void adviceCardHandle (int cardId, String name, int type, String category, int level, String text, int iconId) {

        View cardView = adviceCardViews.get(cardId);

        if (cardView != null) {
            TextView textView = cardView.findViewById(R.id.advice_text);
            ImageView imageView = cardView.findViewById(R.id.advice_image);
            textView.setText(category);
            imageView.setImageResource(iconId);
        }
    }
    private void cardHandler(int cardId, String name, String value, String desc, int iconId) {

        View cardView = cardViews.get(cardId);
        ImageView cardImage = cardView.findViewById(R.id.card_image);
        TextView cardName = cardView.findViewById(R.id.card_name);
        TextView cardNumber = cardView.findViewById(R.id.card_number);
        TextView cardDesc = cardView.findViewById(R.id.card_description);

        if (cardView != null) {
            cardImage.setImageResource(iconId);
            cardName.setText(name);
            cardNumber.setText(value);
            cardDesc.setText(desc);
        }
    }

    private void startAutoScroll() {
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable);
    }
    private void startCarousel(List<CarouselItem> carouselItemList) {

        // 检查 Fragment 是否附加到 Activity
        if (getActivity() == null || getActivity().isFinishing() || !isAdded()) {
            return;
        }

        // 停止之前的轮播
        stopAutoScroll();

        // 清除所有已安排的Runnable
        handler.removeCallbacksAndMessages(null);

        // 优化性能：适配器复用
        if (adapter == null) {
            adapter = new CarouselAdapter(carouselItemList);
            binding.viewPager.setAdapter(adapter);

            // 设置初始位置（实现无限循环）
            binding.viewPager.setCurrentItem(INITIAL_POSITION, false);
            currentPage = INITIAL_POSITION;

            // 添加页面变化监听器
            binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    currentPage = position;
                }
            });
        } else {
            // 更新现有适配器的数据
            adapter.updateData(carouselItemList);
        }

        adapter.setOnItemClickListener(new CarouselAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, CarouselItem item) {
                // 创建底部弹窗
                // 使用 Activity 的 Context 而不是 Application Context
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());

                // 加载布局
                View bottomSheetView = LayoutInflater.from(getActivity())
                        .inflate(R.layout.bottom_sheet_layout, null);
                bottomSheetDialog.setContentView(bottomSheetView);

                // 获取布局中的控件
                TextView title = bottomSheetView.findViewById(R.id.textView);
                TextView details = bottomSheetView.findViewById(R.id.textView2);
                title.setText(item.getTitle());
                details.setText(item.getDetails());
                bottomSheetDialog.show();
            }
        });

        // 设置自动轮播
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (binding == null || binding.getRoot() == null) {
                    return; // 避免操作已销毁的视图
                }
                int itemCount = adapter.getItemCount();
                if (itemCount == 0) return;

                // 使用当前页面+1的方式而不是直接获取当前项
                int nextPage = currentPage + 1;
                binding.viewPager.setCurrentItem(nextPage, true);
                handler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };
        // 延迟启动自动轮播，确保ViewPager2已完成初始化
//        handler.postDelayed(() -> {
//            if (binding != null && binding.getRoot() != null) {
//                handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
//            }
//        }, 100);
    }
}
