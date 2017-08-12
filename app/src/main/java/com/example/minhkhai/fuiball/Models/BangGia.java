package com.example.minhkhai.fuiball.Models;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by minhkhai on 29/07/17.
 */

public class BangGia {

    String ngay, batDau, ketThuc, gia, nguoiDat;
    String trangThai;

    public BangGia(String ngay, String batDau, String ketThuc, String gia, String nguoiDat, String trangThai) {
        this.ngay = ngay;
        this.batDau = batDau;
        this.ketThuc = ketThuc;
        this.gia = gia;
        this.nguoiDat = nguoiDat;
        this.trangThai = trangThai;
    }

    public BangGia(String ngay, String batDau, String ketThuc, String gia, String trangThai) {
        this.ngay = ngay;
        this.batDau = batDau;
        this.ketThuc = ketThuc;
        this.gia = gia;
        this.trangThai = trangThai;
    }

    public BangGia(String ngay, String batDau, String ketThuc, String trangThai) {
        this.ngay = ngay;
        this.batDau = batDau;
        this.ketThuc = ketThuc;
        this.trangThai = trangThai;
    }

    public String getNguoiDat() {
        return nguoiDat;
    }

    public void setNguoiDat(String nguoiDat) {
        this.nguoiDat = nguoiDat;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public String getBatDau() {
        return batDau;
    }

    public void setBatDau(String batDau) {
        this.batDau = batDau;
    }

    public String getKetThuc() {
        return ketThuc;
    }

    public void setKetThuc(String ketThuc) {
        this.ketThuc = ketThuc;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String isTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    // chuyen doi tuong bang gia thanh dang json object
    public JSONObject toJsonObject(){
        String strObj = "{'date':'" + ngay +
                "', 'from':'" + batDau +
                "', 'to':'" + ketThuc +
                "', 'price':'" + gia +
                "', 'user_id':'" + nguoiDat +
                "', 'status':'pending'}";
        Log.i("chuoi", strObj);
        try {
            return new JSONObject(strObj);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
