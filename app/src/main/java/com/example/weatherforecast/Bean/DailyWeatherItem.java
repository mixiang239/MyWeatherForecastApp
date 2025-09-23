package com.example.weatherforecast.Bean;

import java.io.Serializable;

public class DailyWeatherItem implements Serializable {
    // 日期
    String date;
    // 图片资源Id
    int ImageId;
    // 最高和最低温
    String MinAndMaxTem;

    public DailyWeatherItem(String date, int ImageId, String maxAndMinTem) {
        this.date = date;
        this.ImageId = ImageId;
        MinAndMaxTem = maxAndMinTem;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    public int getImageId() {
        return ImageId;
    }

    public void setImageId(int imageId) {
        ImageId = imageId;
    }

    public String getMinAndMaxTem() {
        return MinAndMaxTem;
    }

    public void setMinAndMaxTem(String minAndMaxTem) {
        MinAndMaxTem = minAndMaxTem;
    }
}
