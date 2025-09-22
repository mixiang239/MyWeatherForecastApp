package com.example.weatherforecast.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.Bean.DailyWeatherItem;

import java.util.List;

public class DailyWeatherAdapter extends RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder> {
    private List<DailyWeatherItem> dailyWeatherList;

    public DailyWeatherAdapter(List<DailyWeatherItem> dailyWeatherList) {
        this.dailyWeatherList = dailyWeatherList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.daily_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DailyWeatherItem currentWea = dailyWeatherList.get(position);
        holder.DateText.setText(currentWea.getDate());
        holder.WeatherIcon.setImageResource(currentWea.getImageId());
        holder.MaxAndMinText.setText(currentWea.getMinAndMaxTem());
    }

    @Override
    public int getItemCount() {
        return dailyWeatherList == null ? 0 : dailyWeatherList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView DateText;
        ImageView WeatherIcon;
        TextView MaxAndMinText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            DateText = itemView.findViewById(R.id.daily_date);
            WeatherIcon = itemView.findViewById(R.id.daily_icon);
            MaxAndMinText = itemView.findViewById(R.id.daily_tem);
        }
    }
}
