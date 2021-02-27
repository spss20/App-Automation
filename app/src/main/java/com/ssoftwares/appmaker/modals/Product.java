package com.ssoftwares.appmaker.modals;

import android.os.Parcel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Product implements Serializable {
    private String id;
    private String name;
    private String description;
    private String short_description;
    private String slug;
    private String created_at;
    private List<Attachment> images;
    private List<Category> categories;
    private Attachment brochure;
    private boolean isAutomated;
    private boolean isAdminPanel;

    public boolean isAdminPanel() {
        return isAdminPanel;
    }

    public void setAdminPanel(boolean adminPanel) {
        isAdminPanel = adminPanel;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<Attachment> getImages() {
        return images;
    }

    public void setImages(List<Attachment> images) {
        this.images = images;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        description = in.readString();
        slug = in.readString();
        created_at = in.readString();
        if (in.readByte() == 0x01) {
            images = new ArrayList<Attachment>();
            in.readList(images, Attachment.class.getClassLoader());
        } else {
            images = null;
        }
    }

    public String getShort_description() {
        return short_description;
    }

    public void setShort_description(String short_description) {
        this.short_description = short_description;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public boolean isAutomated() {
        return isAutomated;
    }

    public void setAutomated(boolean automated) {
        isAutomated = automated;
    }

    public Attachment getBrochure() {
        return brochure;
    }

    public void setBrochure(Attachment brochure) {
        this.brochure = brochure;
    }
}