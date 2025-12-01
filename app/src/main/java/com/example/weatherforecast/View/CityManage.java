package com.example.weatherforecast.View;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.room.Room;

import com.example.weatherforecast.Adapter.CityCardAdapter;
import com.example.weatherforecast.Adapter.CityListAdapter;
import com.example.weatherforecast.Bean.City;
import com.example.weatherforecast.Bean.CityCardItem;
import com.example.weatherforecast.DataBase.CityDataBase;
import com.example.weatherforecast.Model.weatherModel;
import com.example.weatherforecast.R;
import com.example.weatherforecast.ViewModel.searchCityWeatherViewModel;
import com.example.weatherforecast.ViewModel.weatherViewModel;
import com.example.weatherforecast.databinding.CityBinding;
import com.example.weatherforecast.location.WeatherApp;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CityManage extends AppCompatActivity implements View.OnClickListener {
    private CityBinding binding;
    private searchCityWeatherViewModel searchCityWeatherViewModel;
    private CityDataBase cityDataBase;
    CityCardAdapter adapter;
    LinearLayoutManager verticalLayoutManager;
    private static final String TAG = "CityManage";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        // 初始化ViewBinding
        binding = CityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 初始化适配器
        verticalLayoutManager = new LinearLayoutManager(CityManage.this);
        List<CityCardItem> cityCardItemList = new ArrayList<>();
        adapter = new CityCardAdapter(cityCardItemList);
        binding.cityCardRecyclerView.setLayoutManager(verticalLayoutManager);
        binding.cityCardRecyclerView.setAdapter(adapter);

        // 设置点击事件监听器
        adapter.setOnItemClickListener(position -> {
            // 处理点击事件

            // 直接启动MainActivity并传递数据
            Intent mainIntent = new Intent(CityManage.this, MainActivity.class);
            mainIntent.putExtra("city_added", false);
            mainIntent.putExtra("city_name_true", cityCardItemList.get(position).getName());
            startActivity(mainIntent);
            finish(); // 结束当前活动

        });

        // 初始化数据库
        cityDataBase = Room.databaseBuilder(WeatherApp.getContext(), CityDataBase.class, "city-database.db")
                .addMigrations(CityDataBase.MIGRATION_1_2)
                .build();


        View locatedCityCard = binding.locatedCardItem;
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
             // 从Bundle中获取数据
             City locatedCity = (City) bundle.getSerializable("weather_data");
             // 设置卡片内容
            if (locatedCity != null) {
                binding.cityItemNameCur.setText(locatedCity.getName());
                binding.cityItemTemperatureCur.setText(locatedCity.getTemperature());
                binding.cityItemAirQualityCur.setText(locatedCity.getAirQuality());
                binding.cityItemWeatherCur.setText(locatedCity.getWeather());
                locatedCityCard.setOnClickListener(v -> {
                    // 直接启动MainActivity并传递数据
                    Intent mainIntent = new Intent(CityManage.this, MainActivity.class);
                    mainIntent.putExtra("city_added", false);
                    mainIntent.putExtra("city_name_true", locatedCity.getName());
                    startActivity(mainIntent);
                    finish(); // 结束当前活动
                });
            }
        }
        Init();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshCityList();
    }

    private void refreshCityList() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            List<City> cityList = cityDataBase.cityDao().getAllCity();
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
                adapter.updateData(cityCardItemList);
            });
        });
    }

    private void deleteAllCities() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {

            @Override
            public void run() {
                cityDataBase.cityDao().deleteAllCity();
                List<CityCardItem> cityCardItemList = new ArrayList<>();
                runOnUiThread(() -> {
                    adapter.updateData(cityCardItemList);
                    Toast.makeText(CityManage.this, "删除成功!", Toast.LENGTH_SHORT).show();
                });
            }
        });

    }

    private void Init() {
        binding.cityBack.setOnClickListener(this);
        binding.addCity.setOnClickListener(this);
        // 初始化ViewModel
        searchCityWeatherViewModel = new ViewModelProvider(this).get(searchCityWeatherViewModel.class);

        binding.deleteAllCities.setOnClickListener(v -> {

            showMaterialDeleteDialog();
            //deleteAllCities();
        });

        binding.searchEditText.setKeyListener(null); // 阻止键盘输入
        binding.searchEditText.setFocusable(false); // 阻止获得焦点
        binding.searchEditText.setFocusableInTouchMode(false); // 阻止触摸获得焦点
        binding.searchEditText.setOnClickListener(this);

        refreshCityList();

    }
    private void showMaterialDeleteDialog() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle("删除确认")
                .setMessage("删除后数据将永久丢失")
                .setIcon(R.drawable.warning)

                // 删除按钮
                .setPositiveButton("删除", (dialog, which) -> deleteAllCities())

                // 取消按钮
                .setNegativeButton("取消", null);

        // 显示对话框
        AlertDialog dialog = builder.show();

        // 动态设置按钮颜色
        Button deleteBtn = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        deleteBtn.setTextColor(ContextCompat.getColor(this, R.color.button_pink));
    }
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.city_back) {
            finish();
        }
        if (v.getId() == R.id.search_edit_text || v.getId() == R.id.add_city) {
            Intent intent = new Intent(CityManage.this, SearchCity.class);
            startActivity(intent);
        }
    }
}
