package com.example.weatherforecast.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Fragment.CityWeatherFragment;

import java.util.List;

public class CityWeatherPagerAdapter extends FragmentStateAdapter {
    private List<City> cityList;

    public CityWeatherPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<City> cityList) {
        super(fragmentActivity);
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // 获取对应位置的城市数据
        City city = cityList.get(position);
        // 创建并返回CityWeatherFragment实例，传递城市数据
        return CityWeatherFragment.newInstance(city.getData());
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
}
