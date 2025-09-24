package com.example.weatherforecast.Adapter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.DailyWeatherItem;
import com.example.weatherforecast.Fragment.CityWeatherFragment;

import java.util.List;

public class CityWeatherPagerAdapter extends FragmentStateAdapter {
    private List<City> cityList;
    private SparseArray<Fragment> fragments = new SparseArray<>();
    public CityWeatherPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<City> cityList) {
        super(fragmentActivity);
        this.cityList = cityList;
    }

    public void updateData(List<City> cityDataList) {
        this.cityList.clear();
        this.cityList.addAll(cityDataList);
        notifyDataSetChanged();

        // 或者更好的方式：使用DiffUtil进行高效更新
        // DiffUtil.DiffResult result = DiffUtil.calculateDiff(new MyDiffCallback(this.dataList, newData));
        // this.dataList = newData;
        // result.dispatchUpdatesTo(this);
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {

//        // 获取对应位置的城市数据
//        City city = cityList.get(position);
//        // 创建并返回CityWeatherFragment实例，传递城市数据
//        return CityWeatherFragment.newInstance(city.getData());
        // 复用已创建的Fragment实例
        Fragment fragment = fragments.get(position);
        if (fragment == null) {
            City city = cityList.get(position);
            fragment = CityWeatherFragment.newInstance(city.getData());
            fragments.put(position, fragment);
        }
        return fragment;

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }
}
