package com.example.weatherforecast.View;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.weatherforecast.R;
import com.example.weatherforecast.ViewModel.weatherViewModel;
import com.example.weatherforecast.databinding.CityBinding;

public class CityManage extends AppCompatActivity implements View.OnClickListener {
    private CityBinding binding;
    private weatherViewModel weatherViewModel;
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
        weatherViewModel = new ViewModelProvider(this).get(weatherViewModel.class);

        binding.searchEditText.setKeyListener(null); // 阻止键盘输入
        binding.searchEditText.setFocusable(false); // 阻止获得焦点
        binding.searchEditText.setFocusableInTouchMode(false); // 阻止触摸获得焦点
        binding.searchEditText.setOnClickListener(this);

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
