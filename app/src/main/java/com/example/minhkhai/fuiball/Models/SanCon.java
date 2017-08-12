package com.example.minhkhai.fuiball.Models;

import android.util.JsonReader;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by minhkhai on 30/07/17.
 */

public class SanCon{

    String id, name;
    JSONArray order;

    public SanCon(String name) {
        this.name = name;
    }

    public SanCon(String id, String name, JSONArray order) {
        this.id = id;
        this.name = name;
        this.order = order;
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

    public JSONArray getOrder() {
        return order;
    }

    public void setOrder(JSONArray order) {
        this.order = order;
    }

    public void newOrder(JSONObject obj) {
        this.order.put(obj);
    }

    @Override
    public String toString() {
        return getName();
    }
}
