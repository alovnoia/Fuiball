package com.example.minhkhai.fuiball.libs;

/**
 * Created by minhkhai on 26/07/17.
 * Hỗ trợ lưu lại location cho sân bóng
 */

public class myLocation {

    private String lat, longi;

    public myLocation(String lat, String longi) {
        this.lat = lat;
        this.longi = longi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }
}
