package com.example.timofey.wowtest;

import java.io.Serializable;

/**
 * Created by timofey on 04.02.2017.
 */

public class Restaurant implements Serializable {

    private String name;
    private String address;
    private double latitude;
    private double longitude;
    private String category;
    private double distance;
    private String iconUrl;
    private String url;
    private String phone;
    private String formattedPhone;
    private String twitter;

    public Restaurant(String name, String address, double latitude, double longitude, String category, int distance, String iconUrl, String url, String phone, String formattedPhone, String twitter) {
        this.name = name;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.category = category;
        this.distance = distance;
        this.iconUrl = iconUrl;
        this.url = url;
        this.phone = phone;
        this.formattedPhone = formattedPhone;
        this.twitter = twitter;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public String getUrl() {
        return url;
    }

    public String getPhone() {
        return phone;
    }

    public String getFormattedPhone() {
        return formattedPhone;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getCategory() {
        return category;
    }

    public String getDistance() {
        if (distance == 0){
            return null;
        }
        double dist = distance / 1000;
        return String.format("%.2f",dist) + "km";
    }
}
