package com.ssoftwares.appmaker.modals;

import com.google.gson.JsonObject;

import java.util.List;

public class SubProduct {

    String id;
    String name;
    String description;
    JsonObject apischema;
    List<Attachment> images;

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

    public List<Attachment> getImages() {
        return images;
    }
}
