package com.ssoftwares.appmaker.modals.errormodels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


import java.util.List;

public class Message {

    @SerializedName("messages")
    @Expose
    private List<Message_> messages = null;

    public List<Message_> getMessages() {
        return messages;
    }

    public void setMessages(List<Message_> messages) {
        this.messages = messages;
    }

}
