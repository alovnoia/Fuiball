package com.example.minhkhai.fuiball.Models;

import android.location.Location;

import com.example.minhkhai.fuiball.libs.myLocation;

import java.util.List;

/**
 * Created by minhkhai on 26/07/17.
 */

public class SanBong {

    private String id, address, name, phone, area, city, type, desc;
    myLocation location;
    List<DichVu> lstDichVu;
    List<String> sanNho;

    public SanBong(String id, String address, String name, String area, String city) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.area = area;
        this.city = city;
    }

    public SanBong(String id, String address, String name, String area) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.area = area;
    }

    public SanBong(String id, String address, String name, String phone, String area, String city, String type, String desc, myLocation location, List<DichVu> lstDichVu, List<String> sanNho) {
        this.id = id;
        this.address = address;
        this.name = name;
        this.phone = phone;
        this.area = area;
        this.city = city;
        this.type = type;
        this.desc = desc;
        this.location = location;
        this.lstDichVu = lstDichVu;
        this.sanNho = sanNho;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public myLocation getLocation() {
        return location;
    }

    public void setLocation(myLocation location) {
        this.location = location;
    }

    public List<DichVu> getLstDichVu() {
        return lstDichVu;
    }

    public void setLstDichVu(List<DichVu> lstDichVu) {
        this.lstDichVu = lstDichVu;
    }

    public List<String> getSanNho() {
        return sanNho;
    }

    public void setSanNho(List<String> sanNho) {
        this.sanNho = sanNho;
    }
}
