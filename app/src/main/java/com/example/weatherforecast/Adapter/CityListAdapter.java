package com.example.weatherforecast.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.Bean.CityInfo;
import com.example.weatherforecast.R;

import java.util.ArrayList;
import java.util.List;

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.ViewHolder> {
    List<CityInfo> cityList;
    private static OnItemClickListener mListener;

    // 定义点击事件接口
    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    // 设置点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public CityListAdapter(List<CityInfo> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.string_city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityListAdapter.ViewHolder holder, int position) {
        CityInfo item = cityList.get(position);
        String info = item.getName() + "，" + item.getAdm2() + "，"
                + item.getAdm1() + "，" + item.getCountry();
        holder.city.setText(info);
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView city;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            city = itemView.findViewById(R.id.string_city_name);
            // 为 itemView 设置点击事件
            itemView.setOnClickListener(v -> {
                if (mListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        mListener.onItemClick(position);
                    }
                }
            });
        }
    }
}
