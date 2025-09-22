package com.example.weatherforecast.Bean;

public class RealTimeWeatherResponse {
    private String code;
    private String updateTime;
    private String fxLink;
    private ReferBean refer;
    private RealTimeWeatherResponse.nowBean now;

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

    public RealTimeWeatherResponse.nowBean getNow() {
        return now;
    }

    public void setNow(RealTimeWeatherResponse.nowBean now) {
        this.now = now;
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

    public static class nowBean {
        private String obsTime;
        private String temp;
        private String feelsLike;
        private String icon;
        private String text;
        private String wind360;
        private String windDir;
        private String windScale;
        private String windSpeed;
        private String humidity;
        private String precip;
        private String pressure;
        private String vis;
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

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getObsTime() {
            return obsTime;
        }

        public void setObsTime(String obsTime) {
            this.obsTime = obsTime;
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

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
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
            return "nowBean{" +
                    "cloud='" + cloud + '\'' +
                    ", obsTime='" + obsTime + '\'' +
                    ", temp='" + temp + '\'' +
                    ", feelsLike='" + feelsLike + '\'' +
                    ", icon='" + icon + '\'' +
                    ", text='" + text + '\'' +
                    ", wind360='" + wind360 + '\'' +
                    ", windDir='" + windDir + '\'' +
                    ", windScale='" + windScale + '\'' +
                    ", windSpeed='" + windSpeed + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", precip='" + precip + '\'' +
                    ", pressure='" + pressure + '\'' +
                    ", vis='" + vis + '\'' +
                    ", dew='" + dew + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "QueryWeatherResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", fxLink='" + fxLink + '\'' +
                ", refer=" + refer +
                ", now=" + now +
                '}';
    }
}
