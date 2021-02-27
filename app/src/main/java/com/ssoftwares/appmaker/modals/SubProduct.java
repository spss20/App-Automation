package com.ssoftwares.appmaker.modals;

import com.google.gson.JsonObject;

import java.util.List;

public class SubProduct {

    String id;
    String name;
    String description;
    JsonObject apischema;
    List<Attachment> images;

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setApischema(JsonObject apischema) {
        this.apischema = apischema;
    }

    public void setImages(List<Attachment> images) {
        this.images = images;
    }

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
