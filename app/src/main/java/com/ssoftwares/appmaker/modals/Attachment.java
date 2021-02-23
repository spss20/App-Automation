package com.ssoftwares.appmaker.modals;

import com.ssoftwares.appmaker.api.ApiClient;

import java.io.Serializable;

public class Attachment implements Serializable {
    private String width;
    private String height;
    private String name;
    private String url;
    private Formats formats;

    public String getWidth() {
        return width;
    }

    public String getHeight() {
        return height;
    }

    public String getName() {
        return name;
    }

    public String getImageUrl(){
        return ApiClient.BASE_URL + url;
    }

    public String getAttachmentUrl(){
        return url;
    }

    public Formats getFormats() {
        return formats;
    }
}
