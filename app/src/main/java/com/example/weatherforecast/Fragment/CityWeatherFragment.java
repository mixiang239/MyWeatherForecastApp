package com.example.weatherforecast.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.weatherforecast.Adapter.CarouselAdapter;
import com.example.weatherforecast.Adapter.DailyWeatherAdapter;
import com.example.weatherforecast.Adapter.HourlyWeatherAdapter;
import com.example.weatherforecast.Bean.CarouselItem;
import com.example.weatherforecast.Bean.Data;
import com.example.weatherforecast.R;
import com.example.weatherforecast.ViewModel.weatherViewModel;
import com.example.weatherforecast.databinding.AdviceCardFragmentBinding;
import com.example.weatherforecast.databinding.CityWeatherFragmentBinding;
import com.example.weatherforecast.location.WeatherApp;

import java.util.List;

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

    // 添加静态方法创建带参数的Fragment实例
    public static CityWeatherFragment newInstance(Data data) {
        CityWeatherFragment fragment = new CityWeatherFragment();
        Bundle args = new Bundle();
        args.putSerializable("data_key", data);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = CityWeatherFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // 如果有参数，设置内容
        if (getArguments() != null) {
            updateContent( (Data) getArguments().getSerializable("data_key") );
        }
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
    // 添加更新内容的方法
    public void updateContent(Data data) {
        try {
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
                    // 竖向布局管理器
                    LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(WeatherApp.getContext());
                    // 适配器
                    DailyWeatherAdapter dailyWeatherAdapter = new DailyWeatherAdapter(data.getDailyWeatherItemList());
                    binding.dailyRecyclerView.setLayoutManager(verticalLayoutManager);
                    binding.dailyRecyclerView.setAdapter(dailyWeatherAdapter);
                }
                if (data.getHourlyWeatherItemList() != null) {
                    // 逐小时天气预报
                    // 横向布局管理器
                    LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(
                            WeatherApp.getContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                    );
                    // 适配器
                    HourlyWeatherAdapter hourlyWeatherAdapter = new HourlyWeatherAdapter(data.getHourlyWeatherItemList());
                    binding.hourlyRecyclerview.setLayoutManager(horizontalLayoutManager);
                    binding.hourlyRecyclerview.setAdapter(hourlyWeatherAdapter);
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
                    cardHandler(R.id.fragment_container2, "体感温度",
                            data.getFeelsLike() + "°", "", R.drawable.temperature);
                }
                if (data.getHumidity() != null) {
                    cardHandler(R.id.fragment_container3, "湿度",
                            data.getHumidity(), "%", R.drawable.humidity);
                }
                if (data.getWindDir() != null && data.getWindScale() != null) {
                    cardHandler(R.id.fragment_container4, data.getWindDir(),
                            data.getWindScale(), "级", R.drawable.wind);
                }
                if (data.getPressure() != null) {
                    cardHandler(R.id.fragment_container5, "气压",
                            data.getPressure(), "百帕", R.drawable.pressure);
                }
                if (data.getVis() != null) {
                    cardHandler(R.id.fragment_container6, "能见度",
                            data.getVis(), "千米", R.drawable.visibility);
                }
                if (data.getPrecip() != null) {
                    cardHandler(R.id.fragment_container1, "降水量",
                            data.getPrecip(), "mm", R.drawable.precipitation);
                }

                // 设置生活卡片内容
                if (data.getLivingIndexList() != null && data.getLivingIndexList().size() != 0) {
                    List<Data.LivingIndexBean> lists = data.getLivingIndexList();
                    for (int i = 0; i < lists.size(); i++) {
                        switch (Integer.parseInt(lists.get(i).getType())) {
                            case 3: // 穿衣指数
                                adviceCardHandle(R.id.life_advice_fragment_container1, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), dressAdvice[Integer.parseInt(lists.get(i).getLevel()) - 1],
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.coat);
                                break;
                            case 16: // 防晒指数
                                adviceCardHandle(R.id.life_advice_fragment_container2, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), sunProtection[Integer.parseInt(lists.get(i).getLevel()) - 1],
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.umbrella);
                                break;
                            case 13: // 化妆指数
                                adviceCardHandle(R.id.life_advice_fragment_container3, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory(),
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.face);
                                break;
                            case 9: // 感冒指数
                                adviceCardHandle(R.id.life_advice_fragment_container4, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), "感冒" + lists.get(i).getCategory(),
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.cold);
                                break;
                            case 2: // 洗车指数
                                adviceCardHandle(R.id.life_advice_fragment_container5, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "洗车",
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.clean_car);
                                break;
                            case 1: // 运动指数
                                adviceCardHandle(R.id.life_advice_fragment_container6, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), lists.get(i).getCategory() + "户外运动",
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.sport);
                                break;
                            case 15: // 交通指数
                                adviceCardHandle(R.id.life_advice_fragment_container7, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), "交通" + lists.get(i).getCategory(),
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.car_margin);
                                break;
                            case 6: // 旅游指数
                                adviceCardHandle(R.id.life_advice_fragment_container8, lists.get(i).getName(),
                                        Integer.parseInt(lists.get(i).getType()), travelAdvice[Integer.parseInt(lists.get(i).getLevel()) - 1],
                                        Integer.parseInt(lists.get(i).getLevel()), lists.get(i).getText(), R.drawable.travel);
                                break;
                            case 4: // 钓鱼指数
                                adviceCardHandle(R.id.life_advice_fragment_container9, lists.get(i).getName(),
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
            throw new RuntimeException(e);
        }

    }

    private void adviceCardHandle (int containerId, String name, int type, String category, int level, String text, int iconId) {
//        AdviceCardFragment fragment = (AdviceCardFragment) requireActivity().getSupportFragmentManager()
//                .findFragmentById(containerId);

        // 使用当前Fragment的视图查找容器
        ViewGroup container = getView().findViewById(containerId);

        // 清除容器中原有内容
        container.removeAllViews();

        // 创建新的卡片视图
        AdviceCardFragment fragment = AdviceCardFragment.newInstance(category, iconId);

        // 添加Fragment到容器
        getChildFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();

        if (fragment == null) {
            // 创建新的Fragment
            fragment = AdviceCardFragment.newInstance(category,iconId);
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment)
                    .commit();
        } else {
            // 更新已有的Fragment
            fragment.updateContent(category, iconId);
        }
    }
    private void cardHandler(int containerId, String name, String value, String desc, int iconId) {

        // BUG：只有一个页面卡片加载成功
        // 使用当前Fragment的视图查找容器
        ViewGroup container = getView().findViewById(containerId);

        // 清除容器中原有内容
        container.removeAllViews();

        // 创建新的卡片视图
        WeatherCardFragment fragment = WeatherCardFragment.newInstance(name, value, desc, iconId);

        // 添加Fragment到容器
        getChildFragmentManager().beginTransaction()
                .replace(containerId, fragment)
                .commit();

        // 创建或获取Fragment实例
//        WeatherCardFragment fragment = (WeatherCardFragment) requireActivity().getSupportFragmentManager()
//                .findFragmentById(containerId);

        if (fragment == null) {
            // 创建新的Fragment
            fragment = WeatherCardFragment.newInstance(
                    name,
                    value,
                    desc,
                    iconId
            );
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(containerId, fragment)
                    .commit();
        } else {
            // 更新现有的Fragment
            fragment.updateContent(
                    name,
                    value,
                    desc,
                    iconId
            );
        }
    }

    private void startAutoScroll() {
        handler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    private void stopAutoScroll() {
        handler.removeCallbacks(autoScrollRunnable);
    }
    private void startCarousel(List<CarouselItem> carouselItemList) {

        // 初始化视图
        viewPager = binding.viewPager;

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
}
