package com.ssoftwares.appmaker.modals.errormodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message_ {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("message")
    @Expose
    private String message;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
