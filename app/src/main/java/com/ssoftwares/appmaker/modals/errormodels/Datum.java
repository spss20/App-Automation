package com.ssoftwares.appmaker.modals.errormodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class Datum {

    @SerializedName("messages")
    @Expose
    private List<Message__> messages = null;

    public List<Message__> getMessages() {
        return messages;
    }

    public void setMessages(List<Message__> messages) {
        this.messages = messages;
    }

}
