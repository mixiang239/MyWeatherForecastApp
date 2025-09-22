package com.example.weatherforecast.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherforecast.databinding.WeatherCardFragmentBinding;

public class WeatherCardFragment extends Fragment {
    private  WeatherCardFragmentBinding binding;
    ImageView icon;
    TextView name;
    TextView number;
    TextView description;
    // 添加静态方法创建带参数的Fragment实例
    public static WeatherCardFragment newInstance(String name, String value, String desc, int iconResId) {
        WeatherCardFragment fragment = new WeatherCardFragment();
        Bundle args = new Bundle();
        args.putString("name", name);
        args.putString("value", value);
        args.putString("desc", desc);
        args.putInt("icon", iconResId);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = WeatherCardFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化视图组件
        icon = binding.cardImage;
        name = binding.cardName;
        number = binding.cardNumber;
        description = binding.cardDescription;
        // 如果有参数，设置内容
        if (getArguments() != null) {
            updateContent(
                    getArguments().getString("name"),
                    getArguments().getString("value"),
                    getArguments().getString("desc"),
                    getArguments().getInt("icon")
            );
        }
    }
    // 添加更新内容的方法
    public void updateContent(String title, String value, String desc, int iconResId) {
        if (name != null) name.setText(title);
        if (number != null) number.setText(value);
        if (description != null) description.setText(desc);
        if (icon != null && iconResId != 0) icon.setImageResource(iconResId);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
