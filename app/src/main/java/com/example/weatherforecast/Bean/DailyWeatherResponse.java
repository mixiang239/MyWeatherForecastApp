package com.example.weatherforecast.Bean;

import java.util.List;

public class DailyWeatherResponse {
    private String code;
    private String updateTime;
    private String fxLink;
    private ReferBean refer;
    private List<DailyWeatherResponse.dailyBean> daily;
    public static class dailyBean {
        private String fxDate;
        private String sunrise;
        private String sunset;
        private String moonrise;
        private String moonset;
        private String moonPhase;
        private String moonPhaseIcon;
        private String tempMax;
        private String tempMin;
        private int iconDay;
        private String textDay;
        private int iconNight;
        private String textNight;
        private String wind360Day;
        private String windDirDay;
        private String windScaleDay;
        private String windSpeedDay;
        private String wind360Night;
        private String windDirNight;
        private String windScaleNight;
        private String windSpeedNight;
        private String humidity;
        private String precip;
        private String pressure;
        private String vis;
        private String cloud;
        private String uvIndex;

        public String getCloud() {
            return cloud;
        }

        public void setCloud(String cloud) {
            this.cloud = cloud;
        }

        public String getFxDate() {
            return fxDate;
        }

        public void setFxDate(String fxDate) {
            this.fxDate = fxDate;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public int getIconDay() {
            return iconDay;
        }

        public void setIconDay(int iconDay) {
            this.iconDay = iconDay;
        }

        public int getIconNight() {
            return iconNight;
        }

        public void setIconNight(int iconNight) {
            this.iconNight = iconNight;
        }

        public String getMoonPhase() {
            return moonPhase;
        }

        public void setMoonPhase(String moonPhase) {
            this.moonPhase = moonPhase;
        }

        public String getMoonPhaseIcon() {
            return moonPhaseIcon;
        }

        public void setMoonPhaseIcon(String moonPhaseIcon) {
            this.moonPhaseIcon = moonPhaseIcon;
        }

        public String getMoonrise() {
            return moonrise;
        }

        public void setMoonrise(String moonrise) {
            this.moonrise = moonrise;
        }

        public String getMoonset() {
            return moonset;
        }

        public void setMoonset(String moonset) {
            this.moonset = moonset;
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

        public String getSunrise() {
            return sunrise;
        }

        public void setSunrise(String sunrise) {
            this.sunrise = sunrise;
        }

        public String getSunset() {
            return sunset;
        }

        public void setSunset(String sunset) {
            this.sunset = sunset;
        }

        public String getTempMax() {
            return tempMax;
        }

        public void setTempMax(String tempMax) {
            this.tempMax = tempMax;
        }

        public String getTempMin() {
            return tempMin;
        }

        public void setTempMin(String tempMin) {
            this.tempMin = tempMin;
        }

        public String getTextDay() {
            return textDay;
        }

        public void setTextDay(String textDay) {
            this.textDay = textDay;
        }

        public String getTextNight() {
            return textNight;
        }

        public void setTextNight(String textNight) {
            this.textNight = textNight;
        }

        public String getUvIndex() {
            return uvIndex;
        }

        public void setUvIndex(String uvIndex) {
            this.uvIndex = uvIndex;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public String getWind360Day() {
            return wind360Day;
        }

        public void setWind360Day(String wind360Day) {
            this.wind360Day = wind360Day;
        }

        public String getWind360Night() {
            return wind360Night;
        }

        public void setWind360Night(String wind360Night) {
            this.wind360Night = wind360Night;
        }

        public String getWindDirDay() {
            return windDirDay;
        }

        public void setWindDirDay(String windDirDay) {
            this.windDirDay = windDirDay;
        }

        public String getWindDirNight() {
            return windDirNight;
        }

        public void setWindDirNight(String windDirNight) {
            this.windDirNight = windDirNight;
        }

        public String getWindScaleDay() {
            return windScaleDay;
        }

        public void setWindScaleDay(String windScaleDay) {
            this.windScaleDay = windScaleDay;
        }

        public String getWindScaleNight() {
            return windScaleNight;
        }

        public void setWindScaleNight(String windScaleNight) {
            this.windScaleNight = windScaleNight;
        }

        public String getWindSpeedDay() {
            return windSpeedDay;
        }

        public void setWindSpeedDay(String windSpeedDay) {
            this.windSpeedDay = windSpeedDay;
        }

        public String getWindSpeedNight() {
            return windSpeedNight;
        }

        public void setWindSpeedNight(String windSpeedNight) {
            this.windSpeedNight = windSpeedNight;
        }

        @Override
        public String toString() {
            return "dailyBean{" +
                    "cloud='" + cloud + '\'' +
                    ", fxDate='" + fxDate + '\'' +
                    ", sunrise='" + sunrise + '\'' +
                    ", sunset='" + sunset + '\'' +
                    ", moonrise='" + moonrise + '\'' +
                    ", moonset='" + moonset + '\'' +
                    ", moonPhase='" + moonPhase + '\'' +
                    ", moonPhaseIcon='" + moonPhaseIcon + '\'' +
                    ", tempMax='" + tempMax + '\'' +
                    ", tempMin='" + tempMin + '\'' +
                    ", iconDay='" + iconDay + '\'' +
                    ", textDay='" + textDay + '\'' +
                    ", iconNight='" + iconNight + '\'' +
                    ", textNight='" + textNight + '\'' +
                    ", wind360Day='" + wind360Day + '\'' +
                    ", windDirDay='" + windDirDay + '\'' +
                    ", windScaleDay='" + windScaleDay + '\'' +
                    ", windSpeedDay='" + windSpeedDay + '\'' +
                    ", wind360Night='" + wind360Night + '\'' +
                    ", windDirNight='" + windDirNight + '\'' +
                    ", windScaleNight='" + windScaleNight + '\'' +
                    ", windSpeedNight='" + windSpeedNight + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", precip='" + precip + '\'' +
                    ", pressure='" + pressure + '\'' +
                    ", vis='" + vis + '\'' +
                    ", uvIndex='" + uvIndex + '\'' +
                    '}';
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<DailyWeatherResponse.dailyBean> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyWeatherResponse.dailyBean> daily) {
        this.daily = daily;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
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

    public void setUpdateTime(String updateTime) { this.updateTime = updateTime; }

    @Override
    public String toString() {
        return "DailyWeatherResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", fxLink='" + fxLink + '\'' +
                ", refer=" + refer +
                ", daily=" + daily +
                '}';
    }
}
