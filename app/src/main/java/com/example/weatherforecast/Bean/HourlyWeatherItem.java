package com.example.weatherforecast.Bean;

import java.io.Serializable;

public class HourlyWeatherItem implements Serializable {
    String time;
    int ImageId;
    String tem;

    public HourlyWeatherItem(String time, int imageId, String tem) {
        ImageId = imageId;
        this.tem = tem;
        this.time = time;
    }

    public int getImageId() {
        return ImageId;
    }
    
    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getTem() {
        return tem;
    }

    public void setTem(String tem) {
        this.tem = tem;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
