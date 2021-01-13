package com.ssoftwares.appmaker.modals;

import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

public class SubProduct {

    String id;
    String name;
    String description;
    JsonObject apischema;
    List<Image> images;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public JsonObject getApischema() {
        return apischema;
    }

    public List<Image> getImages() {
        return images;
    }
}
