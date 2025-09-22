package com.example.weatherforecast.Bean;

import java.util.List;

public class MinutelyPrecipResponse {

    /**
     * code : 200
     * updateTime : 2021-12-16T18:55+08:00
     * fxLink : https://www.qweather.com
     * summary : 95分钟后雨就停了
     * minutely : [{"fxTime":"2021-12-16T18:55+08:00","precip":"0.15","type":"rain"},{"fxTime":"2021-12-16T19:00+08:00","precip":"0.23","type":"rain"},{"fxTime":"2021-12-16T19:05+08:00","precip":"0.21","type":"rain"},{"fxTime":"2021-12-16T19:10+08:00","precip":"0.17","type":"rain"},{"fxTime":"2021-12-16T19:15+08:00","precip":"0.18","type":"rain"},{"fxTime":"2021-12-16T19:20+08:00","precip":"0.24","type":"rain"},{"fxTime":"2021-12-16T19:25+08:00","precip":"0.31","type":"rain"},{"fxTime":"2021-12-16T19:30+08:00","precip":"0.37","type":"rain"},{"fxTime":"2021-12-16T19:35+08:00","precip":"0.41","type":"rain"},{"fxTime":"2021-12-16T19:40+08:00","precip":"0.43","type":"rain"},{"fxTime":"2021-12-16T19:45+08:00","precip":"0.41","type":"rain"},{"fxTime":"2021-12-16T19:50+08:00","precip":"0.36","type":"rain"},{"fxTime":"2021-12-16T19:55+08:00","precip":"0.32","type":"rain"},{"fxTime":"2021-12-16T20:00+08:00","precip":"0.27","type":"rain"},{"fxTime":"2021-12-16T20:05+08:00","precip":"0.22","type":"rain"},{"fxTime":"2021-12-16T20:10+08:00","precip":"0.17","type":"rain"},{"fxTime":"2021-12-16T20:15+08:00","precip":"0.11","type":"rain"},{"fxTime":"2021-12-16T20:20+08:00","precip":"0.06","type":"rain"},{"fxTime":"2021-12-16T20:25+08:00","precip":"0.0","type":"rain"},{"fxTime":"2021-12-16T20:30+08:00","precip":"0.0","type":"rain"},{"fxTime":"2021-12-16T20:35+08:00","precip":"0.0","type":"rain"},{"fxTime":"2021-12-16T20:40+08:00","precip":"0.0","type":"rain"},{"fxTime":"2021-12-16T20:45+08:00","precip":"0.0","type":"rain"},{"fxTime":"2021-12-16T20:50+08:00","precip":"0.0","type":"rain"}]
     * refer : {"sources":["QWeather"],"license":["QWeather Developers License"]}
     */

    private String code;
    private String updateTime;
    private String fxLink;
    private String summary;
    private ReferBean refer;
    private List<MinutelyBean> minutely;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getFxLink() {
        return fxLink;
    }

    public void setFxLink(String fxLink) {
        this.fxLink = fxLink;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public ReferBean getRefer() {
        return refer;
    }

    public void setRefer(ReferBean refer) {
        this.refer = refer;
    }

    public List<MinutelyBean> getMinutely() {
        return minutely;
    }

    public void setMinutely(List<MinutelyBean> minutely) {
        this.minutely = minutely;
    }

    public static class ReferBean {
        private List<String> sources;
        private List<String> license;

        public List<String> getSources() {
            return sources;
        }

        public void setSources(List<String> sources) {
            this.sources = sources;
        }

        public List<String> getLicense() {
            return license;
        }

        public void setLicense(List<String> license) {
            this.license = license;
        }
    }

    public static class MinutelyBean {
        /**
         * fxTime : 2021-12-16T18:55+08:00
         * precip : 0.15
         * type : rain
         */

        private String fxTime;
        private String precip;
        private String type;

        public String getFxTime() {
            return fxTime;
        }

        public void setFxTime(String fxTime) {
            this.fxTime = fxTime;
        }

        public String getPrecip() {
            return precip;
        }

        public void setPrecip(String precip) {
            this.precip = precip;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
}
