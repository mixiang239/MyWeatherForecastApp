package com.example.weatherforecast.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.weatherforecast.Adapter.CityListAdapter;
import com.example.weatherforecast.Bean.CityInfo;
import com.example.weatherforecast.Model.weatherModel;
import com.example.weatherforecast.ViewModel.searchCityWeatherViewModel;
import com.example.weatherforecast.ViewModel.weatherViewModel;
import com.example.weatherforecast.databinding.CityShowBinding;

import java.util.List;

public class SearchCity extends AppCompatActivity {
    private CityShowBinding binding;
    private weatherViewModel weatherViewModel;
    private searchCityWeatherViewModel searchCityWeatherViewModel;
    private List<CityInfo> cityList;
    private static final String API_HOST = "kh487rae6k.re.qweatherapi.com";     // 实际API Host
    private static final String API_KEY = "8dc3ea33ad3b43dcb46bcc08b0bb8337";       // 实际API Key
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = CityShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Init();
        EditTextListener();
    }

    private void Init() {
        // 初始化ViewModel
        weatherViewModel = new ViewModelProvider(this).get(weatherViewModel.class);
        searchCityWeatherViewModel = new ViewModelProvider(this).get(searchCityWeatherViewModel.class);
        // 1. 请求焦点
        binding.searchCityListEditText.requestFocus();

        // 2. 自动弹出软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(binding.searchCityListEditText, InputMethodManager.SHOW_IMPLICIT);

    }
    private void EditTextListener() {

        binding.searchCityListEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // 文本改变前的回调
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // 文本改变时的回调
            }

            @Override
            public void afterTextChanged(Editable s) {
                // 文本改变后的回调
                if (s.toString().isEmpty()) {
                    if (cityList != null && cityList.size() != 0) {
                        cityList.clear();
                    }
                } else {
                    new Handler().postDelayed(() -> {
                        getCityList(s.toString());
                    }, 500); // 延迟0.5秒
                }
            }
        });
    }

    // 获取城市列表并展示
    private void getCityList(String name) {
        weatherViewModel.getCityList().observe(this, response -> {
            if (response != null && response.size() != 0) {
                cityList = response;
                // 竖向布局管理器
                LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this);
                // 适配器
                CityListAdapter adapter = new CityListAdapter(cityList);
                binding.stringCityShowList.setLayoutManager(verticalLayoutManager);
                binding.stringCityShowList.setAdapter(adapter);

                // 设置点击事件监听器
                adapter.setOnItemClickListener(position -> {
                    // 处理点击事件
                    String LocationId = cityList.get(position).getLocationID();
                    String Latitude = cityList.get(position).getLatitude();
                    String Longitude = cityList.get(position).getLongitude();
                    weatherModel.NetworkRequestAPI searchCity = weatherModel.RetrofitClient.getClient("https://" + API_HOST)
                            .create(weatherModel.NetworkRequestAPI.class);

                    searchCityWeatherViewModel.getData(searchCity, LocationId, Double.parseDouble(Latitude), Double.parseDouble(Longitude), cityList.get(position).getName());

                    // 添加成功后，直接启动MainActivity并传递数据
                    Intent mainIntent = new Intent(SearchCity.this, MainActivity.class);
                    mainIntent.putExtra("city_added", true);
                    mainIntent.putExtra("city_name", cityList.get(position).getName());
                    startActivity(mainIntent);
                    finish(); // 结束当前活动

                });
            }
        });

        weatherViewModel.searchCityList(name);
    }
}
