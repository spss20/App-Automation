package com.ssoftwares.appmaker.modals.errormodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ssoftwares.appmaker.modals.errormodels.Datum;
import com.ssoftwares.appmaker.modals.errormodels.Message;

import java.util.List;

public class BaseError {


    public BaseError() {
    }

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("error")
    @Expose
    private String error;
    @SerializedName("message")
    @Expose
    private List<Message> message = null;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public List<Message> getMessage() {
        return message;
    }

    public void setMessage(List<Message> message) {
        this.message = message;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}



