package com.example.minhkhai.fuiball.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhkhai on 27/07/17.
 */

public class ThanhPho {

    String id, name;
    List<String> lstKhuVuc = new ArrayList<>();

    public ThanhPho(String id, String name, List<String> lstKhuVuc) {
        this.id = id;
        this.name = name;
        this.lstKhuVuc = lstKhuVuc;
    }

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

    public List<String> getLstKhuVuc() {
        return lstKhuVuc;
    }

    public void setLstKhuVuc(List<String> lstKhuVuc) {
        this.lstKhuVuc = lstKhuVuc;
    }

    @Override
    public String toString() {
        return getName();
    }
}
