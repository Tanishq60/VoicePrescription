package com.example.voiceprescription.models;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Chat {
    String message;
    boolean sender;
    Date time;

    public Chat() {
    }

    public Chat(boolean sender, Date time, String message) {
        this.message = message;
        this.sender = sender;
        this.time = time;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSender() {
        return sender;
    }

    public void setSender(boolean sender) {
        this.sender = sender;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
