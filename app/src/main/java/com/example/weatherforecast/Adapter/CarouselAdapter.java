package com.example.weatherforecast.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherforecast.R;
import com.example.weatherforecast.Bean.CarouselItem;

import java.util.ArrayList;
import java.util.List;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
    private List<CarouselItem> carouselItemList = new ArrayList<>();
    private static final String TAG = "CarouselAdapter";

    public CarouselAdapter(List<CarouselItem> carouselItemList) {
        this.carouselItemList = carouselItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_carousel, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if (carouselItemList == null || carouselItemList.isEmpty()) {
//                return;
//            }
//        // 使用取模运算实现无限循环
//        int actualPosition = position % carouselItemList.size();
//        CarouselItem item = carouselItemList.get(actualPosition);
//
//        holder.icon.setImageResource(item.getIconId());
//        holder.title.setText(item.getTitle());
//        holder.details.setText(item.getDetails());

        try {
            if (carouselItemList == null || carouselItemList.isEmpty()) {
                return;
            }

            // 使用取模运算实现无限循环
            int actualPosition = position % carouselItemList.size();
            CarouselItem item = carouselItemList.get(actualPosition);

            switch (item.getItemType()) {
                case CarouselItem.WARNING_TYPE:
                    holder.icon.setImageResource(R.drawable.warning);
                    holder.title.setText(item.getTitle());
                    holder.details.setText(item.getDetails());
                    break;
                case CarouselItem.PRECIPITATION_TYPE:
                    holder.icon.setImageResource(R.drawable.rain);
                    holder.title.setText(item.getTitle());
                    holder.details.setText(item.getDetails());
                    break;
                case CarouselItem.ADVICE_TYPE:
                    holder.icon.setImageResource(R.drawable.clothes1);
                    holder.title.setText(item.getTitle());
                    holder.details.setText(item.getDetails());
                    break;
                case CarouselItem.AIR_TYPE:
                    holder.icon.setImageResource(R.drawable.air);
                    holder.title.setText(item.getTitle());
                    holder.details.setText(item.getDetails());
                    break;
                default:
            }
        } catch (Exception e) {
            Log.d(TAG, "onBindViewHolder: " + e.getMessage());
        }

    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title;
        TextView details;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.carousel_icon);
            title = itemView.findViewById(R.id.carousel_title);
            details = itemView.findViewById(R.id.carousel_details);
        }
    }
}
