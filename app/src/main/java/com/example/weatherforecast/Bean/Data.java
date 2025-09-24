package com.example.weatherforecast.Bean;

import java.io.Serializable;
import java.util.List;

public class Data implements Serializable {
    private String RealTimeTem;
    private String RealTimeText;
    private List<HourlyWeatherItem> hourlyWeatherItemList;
    private List<DailyWeatherItem> dailyWeatherItemList;
    private AirQualityBean airQuality;
    private List<LivingIndexBean> livingIndexList;
    private List<WarningInfoBean> warningInfoBean;
    /**
     * 体感温度
     */
    private String feelsLike;
    /**
     * 相对湿度
     */
    private String humidity;
    private String windDir;
    /**
     * 风力等级
     */
    private String windScale;
    private String pressure;

    /**
     * 能见度
     */
    private String vis;
    /**
     * 降水量
     */
    private String precip;
    private List<CarouselItem> carouselItemList;
    private String minutelyPrecip;
    private String sportAdvice;
    private String LocationId;
    /**
     * 纬度信息
     */
    private double latitude;
    /**
     * 经度信息
     */
    private double longitude;
    private String cityName;

    public Data() {}

    public Data(AirQualityBean airQuality, List<CarouselItem> carouselItemList, List<DailyWeatherItem> dailyWeatherItemList, String feelsLike, List<HourlyWeatherItem> hourlyWeatherItemList, String humidity, List<LivingIndexBean> livingIndexList, String minutelyPrecip, String precip, String pressure, String realTimeTem, String realTimeText, String sportAdvice, String vis, List<WarningInfoBean> warningInfoBean, String windDir, String windScale) {
        this.airQuality = airQuality;
        this.carouselItemList = carouselItemList;
        this.dailyWeatherItemList = dailyWeatherItemList;
        this.feelsLike = feelsLike;
        this.hourlyWeatherItemList = hourlyWeatherItemList;
        this.humidity = humidity;
        this.livingIndexList = livingIndexList;
        this.minutelyPrecip = minutelyPrecip;
        this.precip = precip;
        this.pressure = pressure;
        RealTimeTem = realTimeTem;
        RealTimeText = realTimeText;
        this.sportAdvice = sportAdvice;
        this.vis = vis;
        this.warningInfoBean = warningInfoBean;
        this.windDir = windDir;
        this.windScale = windScale;
    }

    public List<DailyWeatherItem> getDailyWeatherItemList() {
        return dailyWeatherItemList;
    }

    public void setDailyWeatherItemList(List<DailyWeatherItem> dailyWeatherItemList) {
        this.dailyWeatherItemList = dailyWeatherItemList;
    }

    public List<HourlyWeatherItem> getHourlyWeatherItemList() {
        return hourlyWeatherItemList;
    }

    public void setHourlyWeatherItemList(List<HourlyWeatherItem> hourlyWeatherItemList) {
        this.hourlyWeatherItemList = hourlyWeatherItemList;
    }

    public String getRealTimeTem() {
        return RealTimeTem;
    }

    public void setRealTimeTem(String realTimeTem) {
        RealTimeTem = realTimeTem;
    }

    public String getRealTimeText() {
        return RealTimeText;
    }

    public void setRealTimeText(String realTimeText) {
        RealTimeText = realTimeText;
    }

    public AirQualityBean getAirQuality() {
        return airQuality;
    }

    public void setAirQuality(AirQualityBean airQuality) {
        this.airQuality = airQuality;
    }

    public String getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(String feelsLike) {
        this.feelsLike = feelsLike;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getVis() {
        return vis;
    }

    public void setVis(String vis) {
        this.vis = vis;
    }

    public String getWindDir() {
        return windDir;
    }

    public void setWindDir(String windDir) {
        this.windDir = windDir;
    }

    public String getWindScale() {
        return windScale;
    }

    public void setWindScale(String windScale) {
        this.windScale = windScale;
    }

    public String getPrecip() {
        return precip;
    }

    public void setPrecip(String precip) {
        this.precip = precip;
    }

    public List<LivingIndexBean> getLivingIndexList() {
        return livingIndexList;
    }

    public void setLivingIndexList(List<LivingIndexBean> livingIndexList) {
        this.livingIndexList = livingIndexList;
    }

    public List<WarningInfoBean> getWarningInfoBean() {
        return warningInfoBean;
    }

    public void setWarningInfoBean(List<WarningInfoBean> warningInfoBean) {
        this.warningInfoBean = warningInfoBean;
    }

    public List<CarouselItem> getCarouselItemList() {
        return carouselItemList;
    }

    public void setCarouselItemList(List<CarouselItem> carouselItemList) {
        this.carouselItemList = carouselItemList;
    }

    public String getMinutelyPrecip() {
        return minutelyPrecip;
    }

    public void setMinutelyPrecip(String minutelyPrecip) {
        this.minutelyPrecip = minutelyPrecip;
    }

    public String getSportAdvice() {
        return sportAdvice;
    }

    public void setSportAdvice(String sportAdvice) {
        this.sportAdvice = sportAdvice;
    }

    public String getLocationId() {
        return LocationId;
    }

    public void setLocationId(String locationId) {
        LocationId = locationId;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public static class AirQualityBean implements Serializable {
        private String aqiDisplay;
        private String category;
        private String advice;
        private int red;
        private int green;
        private int blue;
        private int alpha;

        public AirQualityBean() {}

        public String getAdvice() {
            return advice;
        }

        public void setAdvice(String advice) {
            this.advice = advice;
        }

        public int getAlpha() {
            return alpha;
        }

        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public String getAqiDisplay() {
            return aqiDisplay;
        }

        public void setAqiDisplay(String aqiDisplay) {
            this.aqiDisplay = aqiDisplay;
        }

        public int getBlue() {
            return blue;
        }

        public void setBlue(int blue) {
            this.blue = blue;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public int getGreen() {
            return green;
        }

        public void setGreen(int green) {
            this.green = green;
        }

        public int getRed() {
            return red;
        }

        public void setRed(int red) {
            this.red = red;
        }

        @Override
        public String toString() {
            return "AirQualityBean{" +
                    "advice='" + advice + '\'' +
                    ", aqiDisplay='" + aqiDisplay + '\'' +
                    ", category='" + category + '\'' +
                    ", red=" + red +
                    ", green=" + green +
                    ", blue=" + blue +
                    ", alpha=" + alpha +
                    '}';
        }
    }
    public static class LivingIndexBean implements Serializable {
        private String type;
        private String level;

        private String name;
        private String category; // 级别描述信息
        private String text;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "LivingIndexBean{" +
                    "category='" + category + '\'' +
                    ", type='" + type + '\'' +
                    ", level='" + level + '\'' +
                    ", name='" + name + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    public static class WarningInfoBean implements Serializable {
        private String title;
        private String WarningInfoText;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getWarningInfoText() {
            return WarningInfoText;
        }

        public void setWarningInfoText(String warningInfoText) {
            WarningInfoText = warningInfoText;
        }

        @Override
        public String toString() {
            return "WarningInfoBean{" +
                    "title='" + title + '\'' +
                    ", WarningInfoText='" + WarningInfoText + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "Data{" +
                "airQuality=" + airQuality +
                ", RealTimeTem='" + RealTimeTem + '\'' +
                ", RealTimeText='" + RealTimeText + '\'' +
                ", hourlyWeatherItemList=" + hourlyWeatherItemList +
                ", dailyWeatherItemList=" + dailyWeatherItemList +
                ", livingIndexList=" + livingIndexList +
                ", warningInfoBean=" + warningInfoBean +
                ", feelsLike='" + feelsLike + '\'' +
                ", humidity='" + humidity + '\'' +
                ", windDir='" + windDir + '\'' +
                ", windScale='" + windScale + '\'' +
                ", pressure='" + pressure + '\'' +
                ", vis='" + vis + '\'' +
                ", precip='" + precip + '\'' +
                ", carouselItemList=" + carouselItemList +
                ", minutelyPrecip='" + minutelyPrecip + '\'' +
                ", sportAdvice='" + sportAdvice + '\'' +
                '}';
    }
}
