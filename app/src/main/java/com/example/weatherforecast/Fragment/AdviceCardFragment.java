package com.example.weatherforecast.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.weatherforecast.databinding.AdviceCardFragmentBinding;

public class AdviceCardFragment extends Fragment {
    private AdviceCardFragmentBinding binding;
    ImageView icon;
    TextView category;
    // 添加静态方法创建带参数的Fragment实例
    public static AdviceCardFragment newInstance(String category, int imageResId) {
        AdviceCardFragment fragment = new AdviceCardFragment();
        Bundle args = new Bundle();
        args.putString("category", category);
        args.putInt("icon", imageResId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = AdviceCardFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // 初始化视图组件
        icon = binding.adviceImage;
        category = binding.adviceText;

        // 如果有参数，设置内容
        if (getArguments() != null) {
            updateContent(
                    getArguments().getString("category"),
                    getArguments().getInt("icon")
            );
        }
    }

    // 添加更新内容的方法
    public void updateContent(String desc, int iconResId) {
        if (category != null) category.setText(desc);
        if (icon != null && iconResId != 0) icon.setImageResource(iconResId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
