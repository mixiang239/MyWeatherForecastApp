package com.example.weatherforecast.Bean;

import java.util.List;

public class WarningInfoResponse {

    /**
     * code : 200
     * updateTime : 2023-04-03T14:20+08:00
     * fxLink : https://www.qweather.com/severe-weather/shanghai-101020100.html
     * warning : [{"id":"10102010020230403103000500681616","sender":"上海中心气象台","pubTime":"2023-04-03T10:30+08:00","title":"上海中心气象台发布大风蓝色预警[Ⅳ级/一般]","startTime":"2023-04-03T10:30+08:00","endTime":"2023-04-04T10:30+08:00","status":"active","level":"","severity":"Minor","severityColor":"Blue","type":"1006","typeName":"大风","urgency":"","certainty":"","text":"上海中心气象台2023年04月03日10时30分发布大风蓝色预警[Ⅳ级/一般]：受江淮气旋影响，预计明天傍晚以前本市大部地区将出现6级阵风7-8级的东南大风，沿江沿海地区7级阵风8-9级，请注意防范大风对高空作业、交通出行、设施农业等的不利影响。","related":""}]
     * refer : {"sources":["12379"],"license":["QWeather Developers License"]}
     */

    private String code;
    private String updateTime;
    private String fxLink;
    private ReferBean refer;
    private List<WarningBean> warning;

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

    public List<WarningBean> getWarning() {
        return warning;
    }

    public void setWarning(List<WarningBean> warning) {
        this.warning = warning;
    }

    public static class WarningBean {
        /**
         * id : 10102010020230403103000500681616
         * sender : 上海中心气象台
         * pubTime : 2023-04-03T10:30+08:00
         * title : 上海中心气象台发布大风蓝色预警[Ⅳ级/一般]
         * startTime : 2023-04-03T10:30+08:00
         * endTime : 2023-04-04T10:30+08:00
         * status : active
         * level :
         * severity : Minor
         * severityColor : Blue
         * type : 1006
         * typeName : 大风
         * urgency :
         * certainty :
         * text : 上海中心气象台2023年04月03日10时30分发布大风蓝色预警[Ⅳ级/一般]：受江淮气旋影响，预计明天傍晚以前本市大部地区将出现6级阵风7-8级的东南大风，沿江沿海地区7级阵风8-9级，请注意防范大风对高空作业、交通出行、设施农业等的不利影响。
         * related :
         */

        private String id;
        private String sender;
        private String pubTime;
        private String title;
        private String startTime;
        private String endTime;
        private String status;
        private String level;
        private String severity;
        private String severityColor;
        private String type;
        private String typeName;
        private String urgency;
        private String certainty;
        private String text;
        private String related;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSender() {
            return sender;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

        public String getPubTime() {
            return pubTime;
        }

        public void setPubTime(String pubTime) {
            this.pubTime = pubTime;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getSeverityColor() {
            return severityColor;
        }

        public void setSeverityColor(String severityColor) {
            this.severityColor = severityColor;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeName() {
            return typeName;
        }

        public void setTypeName(String typeName) {
            this.typeName = typeName;
        }

        public String getUrgency() {
            return urgency;
        }

        public void setUrgency(String urgency) {
            this.urgency = urgency;
        }

        public String getCertainty() {
            return certainty;
        }

        public void setCertainty(String certainty) {
            this.certainty = certainty;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getRelated() {
            return related;
        }

        public void setRelated(String related) {
            this.related = related;
        }

        @Override
        public String toString() {
            return "WarningBean{" +
                    "certainty='" + certainty + '\'' +
                    ", id='" + id + '\'' +
                    ", sender='" + sender + '\'' +
                    ", pubTime='" + pubTime + '\'' +
                    ", title='" + title + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", endTime='" + endTime + '\'' +
                    ", status='" + status + '\'' +
                    ", level='" + level + '\'' +
                    ", severity='" + severity + '\'' +
                    ", severityColor='" + severityColor + '\'' +
                    ", type='" + type + '\'' +
                    ", typeName='" + typeName + '\'' +
                    ", urgency='" + urgency + '\'' +
                    ", text='" + text + '\'' +
                    ", related='" + related + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "WarningInfoResponse{" +
                "code='" + code + '\'' +
                ", updateTime='" + updateTime + '\'' +
                ", fxLink='" + fxLink + '\'' +
                ", refer=" + refer +
                ", warning=" + warning +
                '}';
    }
}
