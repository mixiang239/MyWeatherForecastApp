package com.example.weatherforecast.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.weatherforecast.Adapter.CityCardAdapter;
import com.example.weatherforecast.Adapter.CityListAdapter;
import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.CityCardItem;
import com.example.weatherforecast.DataBase.CityDataBase;
import com.example.weatherforecast.R;
import com.example.weatherforecast.ViewModel.searchCityWeatherViewModel;
import com.example.weatherforecast.ViewModel.weatherViewModel;
import com.example.weatherforecast.databinding.CityBinding;
import com.example.weatherforecast.location.WeatherApp;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CityManage extends AppCompatActivity implements View.OnClickListener {
    private CityBinding binding;
    private searchCityWeatherViewModel searchCityWeatherViewModel;
    private CityDataBase cityDataBase;
    private static final String TAG = "CityManage";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);


        // 初始化ViewBinding
        binding = CityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Init();
    }

    private void Init() {
        binding.cityBack.setOnClickListener(this);
        binding.addCity.setOnClickListener(this);
        // 初始化ViewModel
        searchCityWeatherViewModel = new ViewModelProvider(this).get(searchCityWeatherViewModel.class);

        binding.searchEditText.setKeyListener(null); // 阻止键盘输入
        binding.searchEditText.setFocusable(false); // 阻止获得焦点
        binding.searchEditText.setFocusableInTouchMode(false); // 阻止触摸获得焦点
        binding.searchEditText.setOnClickListener(this);

        // 初始化数据库
        cityDataBase = Room.databaseBuilder(WeatherApp.getContext(), CityDataBase.class, "city-database.db")
                .addMigrations(CityDataBase.MIGRATION_1_2)
                .build();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {

            @Override
            public void run() {
                List<City> cityList = cityDataBase.cityDao().getAllCity();
                for (City city1 : cityList) {
                    Log.d(TAG, "id = " + city1.getId());
                    Log.d(TAG, "name = " + city1.getName());
                    Log.d(TAG, "tem = " + city1.getTemperature());
                    Log.d(TAG, "data = " + city1.getData());
                }

                List<CityCardItem> cityCardItemList = new ArrayList<>();

                for (City city : cityList) {
                    CityCardItem item = new CityCardItem();
                    item.setName(city.getName());
                    item.setTemp(city.getTemperature());
                    item.setWeather(city.getWeather());
                    item.setAirQuality(city.getAirQuality());
                    cityCardItemList.add(item);
                }

                runOnUiThread(() -> {
                    LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(WeatherApp.getContext());
                    // 适配器
                    CityCardAdapter adapter = new CityCardAdapter(cityCardItemList);
                    binding.cityCardRecyclerView.setLayoutManager(verticalLayoutManager);
                    binding.cityCardRecyclerView.setAdapter(adapter);
                });

            }
        });

//        searchCityWeatherViewModel.getRoomCityList().observe( this, response -> {
//            if (response != null && response.size() != 0) {
//                List<CityCardItem> cityCardItemList = new ArrayList<>();
//
//                for (City city : response) {
//                    CityCardItem item = new CityCardItem();
//                    item.setName(city.getName());
//                    item.setTemp(city.getTemperature());
//                    item.setWeather(city.getWeather());
//                    item.setAirQuality(city.getAirQuality());
//                    cityCardItemList.add(item);
//                }
//
//                LinearLayoutManager verticalLayoutManager = new LinearLayoutManager(this);
//                // 适配器
//                CityCardAdapter adapter = new CityCardAdapter(cityCardItemList);
//                binding.cityCardRecyclerView.setLayoutManager(verticalLayoutManager);
//                binding.cityCardRecyclerView.setAdapter(adapter);
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.city_back) {
            finish();
        }
        if (v.getId() == R.id.search_edit_text) {
            Intent intent = new Intent(CityManage.this, SearchCity.class);
            startActivity(intent);
        }
        if (v.getId() == R.id.add_city) {
            Intent intent = new Intent(CityManage.this, SearchCity.class);
            startActivity(intent);
        }
    }
}
