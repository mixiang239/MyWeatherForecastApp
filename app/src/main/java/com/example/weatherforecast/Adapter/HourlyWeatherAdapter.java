package com.example.weatherforecast.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.Bean.HourlyWeatherItem;

import java.util.List;

public class HourlyWeatherAdapter extends RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder> {
    private List<HourlyWeatherItem> hourlyWeatherItemList;

    public HourlyWeatherAdapter(List<HourlyWeatherItem> hourlyWeatherItemList) {
        this.hourlyWeatherItemList = hourlyWeatherItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.hourly_weather, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HourlyWeatherItem currentWea = hourlyWeatherItemList.get(position);
        holder.TimeText.setText(currentWea.getTime());
        holder.WeatherIcon.setImageResource(currentWea.getImageId());
        holder.TemText.setText(currentWea.getTem());
    }

    @Override
    public int getItemCount() {
        return hourlyWeatherItemList == null ? 0 : hourlyWeatherItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView TimeText;
        ImageView WeatherIcon;
        TextView TemText;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TimeText = itemView.findViewById(R.id.hourly_time);
            WeatherIcon = itemView.findViewById(R.id.hourly_weather_icon);
            TemText = itemView.findViewById(R.id.hourly_tem);
        }
    }
}
