package com.example.weatherforecast.Bean;

import java.util.List;

public class LivingIndexResponse {

    /**
     * code : 200
     * updateTime : 2021-12-16T18:35+08:00
     * fxLink : http://hfx.link/2ax2
     * daily : [{"date":"2021-12-16","type":"1","name":"运动指数","level":"3","category":"较不宜","text":"天气较好，但考虑天气寒冷，风力较强，推荐您进行室内运动，若户外运动请注意保暖并做好准备活动。"},{"date":"2021-12-16","type":"2","name":"洗车指数","level":"3","category":"较不宜","text":"较不宜洗车，未来一天无雨，风力较大，如果执意擦洗汽车，要做好蒙上污垢的心理准备。"}]
     * refer : {"sources":["QWeather"],"license":["QWeather Developers License"]}
     */

    private String code;
    private String updateTime;
    private String fxLink;
    private ReferBean refer;
    private List<DailyBean> daily;

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

    public ReferBean getRefer() {
        return refer;
    }

    public void setRefer(ReferBean refer) {
        this.refer = refer;
    }

    public List<DailyBean> getDaily() {
        return daily;
    }

    public void setDaily(List<DailyBean> daily) {
        this.daily = daily;
    }

    public static class DailyBean {
        /**
         * date : 2021-12-16
         * type : 1
         * name : 运动指数
         * level : 3
         * category : 较不宜
         * text : 天气较好，但考虑天气寒冷，风力较强，推荐您进行室内运动，若户外运动请注意保暖并做好准备活动。
         */

        private String date;
        private String type;
        private String name;
        private String level;
        private String category;
        private String text;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        @Override
        public String toString() {
            return "DailyBean{" +
                    "category='" + category + '\'' +
                    ", date='" + date + '\'' +
                    ", type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    ", level='" + level + '\'' +
                    ", text='" + text + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LivingIndexResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", fxLink='" + fxLink + '\'' +
                ", refer=" + refer +
                ", daily=" + daily +
                '}';
    }
}
