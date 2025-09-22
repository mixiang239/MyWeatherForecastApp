package com.example.weatherforecast.Bean;

import java.util.List;

public class HourlyWeatherResponse {
    private String code;
    private String updateTime;
    private String fxLink;
    private ReferBean refer;
    private List<HourlyWeatherResponse.HourlyBean> hourly;

    public static class HourlyBean {
        private String fxTime;
        private String temp;
        private int icon;
        private String text;
        private String wind360;
        private String windDir;
        private String windScale;
        private String windSpeed;
        private String humidity;
        private String pop;
        private String precip;
        private String pressure;
        private String cloud;
        private String dew;

        public String getCloud() {
            return cloud;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }

        public String getDew() {
            return dew;
        }

        public void setDew(String dew) {
            this.dew = dew;
        }

        public String getFxTime() {
            return fxTime;
        }

        public void setFxTime(String fxTime) {
            this.fxTime = fxTime;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getPressure() {
            return pressure;
        }

        public void setPressure(String pressure) {
            this.pressure = pressure;
        }

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getWind360() {
            return wind360;
        }

        public void setWind360(String wind360) {
            this.wind360 = wind360;
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

        public String getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(String windSpeed) {
            this.windSpeed = windSpeed;
        }

        @Override
        public String toString() {
            return "HourlyBean{" +
                    "cloud='" + cloud + '\'' +
                    ", fxTime='" + fxTime + '\'' +
                    ", temp='" + temp + '\'' +
                    ", icon='" + icon + '\'' +
                    ", text='" + text + '\'' +
                    ", wind360='" + wind360 + '\'' +
                    ", windDir='" + windDir + '\'' +
                    ", windScale='" + windScale + '\'' +
                    ", windSpeed='" + windSpeed + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", pop='" + pop + '\'' +
                    ", precip='" + precip + '\'' +
                    ", pressure='" + pressure + '\'' +
                    ", dew='" + dew + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public List<HourlyWeatherResponse.HourlyBean> getHourly() {
        return hourly;
    }

    public void setHourly(List<HourlyWeatherResponse.HourlyBean> hourly) {
        this.hourly = hourly;
    }

    public ReferBean getRefer() {
        return refer;
    }

    public void setRefer(ReferBean refer) {
        this.refer = refer;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "HourlyWeatherResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", fxLink='" + fxLink + '\'' +
                ", refer=" + refer +
                ", hourly=" + hourly +
                '}';
    }
}