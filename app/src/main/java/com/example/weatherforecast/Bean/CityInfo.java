package com.example.weatherforecast.Bean;

public class CityInfo {
    private String LocationID;
    private String name;
    private String adm1;
    private String adm2;
    private String Country;
    /**
     * 纬度信息
     */
    private String latitude;
    /**
     * 经度信息
     */
    private String longitude;

    public CityInfo() {
    }

    public CityInfo(String locationID, String name, String adm1, String adm2, String country, String latitude, String longitude) {
        LocationID = locationID;
        this.name = name;
        this.adm1 = adm1;
        this.adm2 = adm2;
        Country = country;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLocationID() {
        return LocationID;
    }

    public void setLocationID(String locationID) {
        LocationID = locationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdm1() {
        return adm1;
    }

    public void setAdm1(String adm1) {
        this.adm1 = adm1;
    }

    public String getAdm2() {
        return adm2;
    }

    public void setAdm2(String adm2) {
        this.adm2 = adm2;
    }

    public String getCountry() {
        return Country;
    }

    public void setCountry(String country) {
        Country = country;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
