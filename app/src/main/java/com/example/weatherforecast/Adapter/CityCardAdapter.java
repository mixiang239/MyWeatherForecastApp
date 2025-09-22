package com.example.weatherforecast.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.Bean.CityCardItem;
import com.example.weatherforecast.R;

import java.util.List;

public class CityCardAdapter extends RecyclerView.Adapter<CityCardAdapter.ViewHolder> {
    List<CityCardItem> cityCardItemList;

    public CityCardAdapter(List<CityCardItem> cityCardItemList) {
        this.cityCardItemList = cityCardItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.city_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityCardItem item = cityCardItemList.get(position);
        holder.name.setText(item.getName());
        holder.temp.setText(item.getTemp());
        holder.airQuality.setText(item.getAirQuality());
        holder.weather.setText(item.getWeather());
    }

    @Override
    public int getItemCount() {
        return cityCardItemList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView temp;
        TextView airQuality;
        TextView weather;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.city_item_name);
            temp = itemView.findViewById(R.id.city_item_temperature);
            airQuality = itemView.findViewById(R.id.city_item_air_quality);
            weather = itemView.findViewById(R.id.city_item_weather);
        }
    }
}
