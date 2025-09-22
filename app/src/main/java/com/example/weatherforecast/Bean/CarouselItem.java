package com.example.weatherforecast.Bean;

public class CarouselItem {
    public static final int WARNING_TYPE = 0;
    public static final int PRECIPITATION_TYPE = 1;
    public static final int ADVICE_TYPE = 2;
    public static final int AIR_TYPE = 3;
    private int itemType;
    private int iconId;
    private String title;
    private String details;

    public CarouselItem() {
    }

    public CarouselItem(String details, int iconId, int itemType, String title) {
        this.details = details;
        this.iconId = iconId;
        this.itemType = itemType;
        this.title = title;
    }

    public CarouselItem(String details, int iconId, String title) {
        this.details = details;
        this.iconId = iconId;
        this.title = title;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getItemType() {
        return itemType;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    @Override
    public String toString() {
        return "CarouselItem{" +
                "details='" + details + '\'' +
                ", itemType=" + itemType +
                ", iconId=" + iconId +
                ", title='" + title + '\'' +
                '}';
    }
}
