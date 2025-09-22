package com.example.weatherforecast.Bean;

import java.util.List;

public class AirQualityResponse {

    /**
     * metadata : {"tag":"d75a323239766b831889e8020cba5aca9b90fca5080a1175c3487fd8acb06e84"}
     * indexes : [{"code":"us-epa","name":"AQI (US)","aqi":46,"aqiDisplay":"46","level":"1","category":"Good","color":{"red":0,"green":228,"blue":0,"alpha":1},"primaryPollutant":{"code":"pm2p5","name":"PM 2.5","fullName":"Fine particulate matter (<2.5µm)"},"health":{"effect":"No health effects.","advice":{"generalPopulation":"Everyone can continue their outdoor activities normally.","sensitivePopulation":"Everyone can continue their outdoor activities normally."}}},{"code":"qaqi","name":"QAQI","aqi":0.9,"aqiDisplay":"0.9","level":"1","category":"Excellent","color":{"red":80,"green":240,"blue":230,"alpha":1},"primaryPollutant":{"code":"pm2p5","name":"PM 2.5","fullName":"Fine particulate matter (<2.5µm)"},"health":{"effect":"No health implications.","advice":{"generalPopulation":"Enjoy your outdoor activities.","sensitivePopulation":"Enjoy your outdoor activities."}}}]
     * pollutants : [{"code":"pm2p5","name":"PM 2.5","fullName":"Fine particulate matter (<2.5µm)","concentration":{"value":11,"unit":"μg/m3"},"subIndexes":[{"code":"us-epa","aqi":46,"aqiDisplay":"46"},{"code":"qaqi","aqi":0.9,"aqiDisplay":"0.9"}]},{"code":"pm10","name":"PM 10","fullName":"Inhalable particulate matter (<10µm)","concentration":{"value":12,"unit":"μg/m3"},"subIndexes":[{"code":"us-epa","aqi":12,"aqiDisplay":"12"},{"code":"qaqi","aqi":0.5,"aqiDisplay":"0.5"}]},{"code":"no2","name":"NO2","fullName":"Nitrogen dioxide","concentration":{"value":6.77,"unit":"ppb"},"subIndexes":[{"code":"us-epa","aqi":7,"aqiDisplay":"7"},{"code":"qaqi","aqi":0.1,"aqiDisplay":"0.1"}]},{"code":"o3","name":"O3","fullName":"Ozone","concentration":{"value":0.02,"unit":"ppb"},"subIndexes":[{"code":"us-epa","aqi":21,"aqiDisplay":"21"},{"code":"qaqi","aqi":0.2,"aqiDisplay":"0.2"}]},{"code":"co","name":"CO","fullName":"Carbon monoxide","concentration":{"value":0.25,"unit":"ppm"},"subIndexes":[{"code":"us-epa","aqi":3,"aqiDisplay":"3"},{"code":"qaqi","aqi":0.1,"aqiDisplay":"0.1"}]}]
     * stations : [{"id":"P51762","name":"North Holywood"},{"id":"P58056","name":"Pasadena"},{"id":"P57327","name":"Los Angeles - N. Main Street"}]
     */

    private MetadataBean metadata;
    private List<IndexesBean> indexes;
    private List<PollutantsBean> pollutants;
    private List<StationsBean> stations;

    public MetadataBean getMetadata() {
        return metadata;
    }

    public void setMetadata(MetadataBean metadata) {
        this.metadata = metadata;
    }

    public List<IndexesBean> getIndexes() {
        return indexes;
    }

    public void setIndexes(List<IndexesBean> indexes) {
        this.indexes = indexes;
    }

    public List<PollutantsBean> getPollutants() {
        return pollutants;
    }

    public void setPollutants(List<PollutantsBean> pollutants) {
        this.pollutants = pollutants;
    }

    public List<StationsBean> getStations() {
        return stations;
    }

    public void setStations(List<StationsBean> stations) {
        this.stations = stations;
    }

    public static class MetadataBean {
        /**
         * tag : d75a323239766b831889e8020cba5aca9b90fca5080a1175c3487fd8acb06e84
         */

        private String tag;

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        @Override
        public String toString() {
            return "MetadataBean{" +
                    "tag='" + tag + '\'' +
                    '}';
        }
    }

    public static class IndexesBean {
        /**
         * code : us-epa
         * name : AQI (US)
         * aqi : 46
         * aqiDisplay : 46
         * level : 1
         * category : Good
         * color : {"red":0,"green":228,"blue":0,"alpha":1}
         * primaryPollutant : {"code":"pm2p5","name":"PM 2.5","fullName":"Fine particulate matter (<2.5µm)"}
         * health : {"effect":"No health effects.","advice":{"generalPopulation":"Everyone can continue their outdoor activities normally.","sensitivePopulation":"Everyone can continue their outdoor activities normally."}}
         */

        private String code;
        private String name;
        private int aqi;
        private String aqiDisplay;
        private String level;
        private String category;
        private ColorBean color;
        private PrimaryPollutantBean primaryPollutant;
        private HealthBean health;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAqi() {
            return aqi;
        }

        public void setAqi(int aqi) {
            this.aqi = aqi;
        }

        public String getAqiDisplay() {
            return aqiDisplay;
        }

        public void setAqiDisplay(String aqiDisplay) {
            this.aqiDisplay = aqiDisplay;
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

        public ColorBean getColor() {
            return color;
        }

        public void setColor(ColorBean color) {
            this.color = color;
        }

        public PrimaryPollutantBean getPrimaryPollutant() {
            return primaryPollutant;
        }

        public void setPrimaryPollutant(PrimaryPollutantBean primaryPollutant) {
            this.primaryPollutant = primaryPollutant;
        }

        public HealthBean getHealth() {
            return health;
        }

        public void setHealth(HealthBean health) {
            this.health = health;
        }

        public static class ColorBean {
            /**
             * red : 0
             * green : 228
             * blue : 0
             * alpha : 1
             */

            private int red;
            private int green;
            private int blue;
            private int alpha;

            public int getRed() {
                return red;
            }

            public void setRed(int red) {
                this.red = red;
            }

            public int getGreen() {
                return green;
            }

            public void setGreen(int green) {
                this.green = green;
            }

            public int getBlue() {
                return blue;
            }

            public void setBlue(int blue) {
                this.blue = blue;
            }

            public int getAlpha() {
                return alpha;
            }

            public void setAlpha(int alpha) {
                this.alpha = alpha;
            }

            @Override
            public String toString() {
                return "ColorBean{" +
                        "alpha=" + alpha +
                        ", red=" + red +
                        ", green=" + green +
                        ", blue=" + blue +
                        '}';
            }
        }

        public static class PrimaryPollutantBean {
            /**
             * code : pm2p5
             * name : PM 2.5
             * fullName : Fine particulate matter (<2.5µm)
             */

            private String code;
            private String name;
            private String fullName;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            @Override
            public String toString() {
                return "PrimaryPollutantBean{" +
                        "code='" + code + '\'' +
                        ", name='" + name + '\'' +
                        ", fullName='" + fullName + '\'' +
                        '}';
            }
        }

        public static class HealthBean {
            /**
             * effect : No health effects.
             * advice : {"generalPopulation":"Everyone can continue their outdoor activities normally.","sensitivePopulation":"Everyone can continue their outdoor activities normally."}
             */

            private String effect;
            private AdviceBean advice;

            public String getEffect() {
                return effect;
            }

            public void setEffect(String effect) {
                this.effect = effect;
            }

            public AdviceBean getAdvice() {
                return advice;
            }

            public void setAdvice(AdviceBean advice) {
                this.advice = advice;
            }

            public static class AdviceBean {
                /**
                 * generalPopulation : Everyone can continue their outdoor activities normally.
                 * sensitivePopulation : Everyone can continue their outdoor activities normally.
                 */

                private String generalPopulation;
                private String sensitivePopulation;

                public String getGeneralPopulation() {
                    return generalPopulation;
                }

                public void setGeneralPopulation(String generalPopulation) {
                    this.generalPopulation = generalPopulation;
                }

                public String getSensitivePopulation() {
                    return sensitivePopulation;
                }

                public void setSensitivePopulation(String sensitivePopulation) {
                    this.sensitivePopulation = sensitivePopulation;
                }

                @Override
                public String toString() {
                    return "AdviceBean{" +
                            "generalPopulation='" + generalPopulation + '\'' +
                            ", sensitivePopulation='" + sensitivePopulation + '\'' +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "HealthBean{" +
                        "advice=" + advice +
                        ", effect='" + effect + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "IndexesBean{" +
                    "aqi=" + aqi +
                    ", code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", aqiDisplay='" + aqiDisplay + '\'' +
                    ", level='" + level + '\'' +
                    ", category='" + category + '\'' +
                    ", color=" + color +
                    ", primaryPollutant=" + primaryPollutant +
                    ", health=" + health +
                    '}';
        }
    }

    public static class PollutantsBean {
        /**
         * code : pm2p5
         * name : PM 2.5
         * fullName : Fine particulate matter (<2.5µm)
         * concentration : {"value":11,"unit":"μg/m3"}
         * subIndexes : [{"code":"us-epa","aqi":46,"aqiDisplay":"46"},{"code":"qaqi","aqi":0.9,"aqiDisplay":"0.9"}]
         */

        private String code;
        private String name;
        private String fullName;
        private ConcentrationBean concentration;
        private List<SubIndexesBean> subIndexes;

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public ConcentrationBean getConcentration() {
            return concentration;
        }

        public void setConcentration(ConcentrationBean concentration) {
            this.concentration = concentration;
        }

        public List<SubIndexesBean> getSubIndexes() {
            return subIndexes;
        }

        public void setSubIndexes(List<SubIndexesBean> subIndexes) {
            this.subIndexes = subIndexes;
        }

        public static class ConcentrationBean {
            /**
             * value : 11
             * unit : μg/m3
             */

            private double value;
            private String unit;

            public double getValue() {
                return value;
            }

            public void setValue(double value) {
                this.value = value;
            }

            public String getUnit() {
                return unit;
            }

            public void setUnit(String unit) {
                this.unit = unit;
            }

            @Override
            public String toString() {
                return "ConcentrationBean{" +
                        "unit='" + unit + '\'' +
                        ", value=" + value +
                        '}';
            }
        }

        public static class SubIndexesBean {
            /**
             * code : us-epa
             * aqi : 46
             * aqiDisplay : 46
             */

            private String code;
            private int aqi;
            private String aqiDisplay;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public int getAqi() {
                return aqi;
            }

            public void setAqi(int aqi) {
                this.aqi = aqi;
            }

            public String getAqiDisplay() {
                return aqiDisplay;
            }

            public void setAqiDisplay(String aqiDisplay) {
                this.aqiDisplay = aqiDisplay;
            }

            @Override
            public String toString() {
                return "SubIndexesBean{" +
                        "aqi=" + aqi +
                        ", code='" + code + '\'' +
                        ", aqiDisplay='" + aqiDisplay + '\'' +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "PollutantsBean{" +
                    "code='" + code + '\'' +
                    ", name='" + name + '\'' +
                    ", fullName='" + fullName + '\'' +
                    ", concentration=" + concentration +
                    ", subIndexes=" + subIndexes +
                    '}';
        }
    }

    public static class StationsBean {
        /**
         * id : P51762
         * name : North Holywood
         */

        private String id;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "StationsBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "AirQualityResponse{" +
                "indexes=" + indexes +
                ", metadata=" + metadata +
                ", pollutants=" + pollutants +
                ", stations=" + stations +
                '}';
    }
}
