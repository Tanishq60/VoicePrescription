package com.example.voiceprescription.models;

import com.google.firebase.Timestamp;

public class Request {
    String from, docUrl;
    Timestamp timeStamp;

    public Request() {
    }

    public Request(String from, String docUrl,String fileUrl, Timestamp timeStamp) {
        this.from = from;
        this.docUrl = docUrl;
        this.timeStamp = timeStamp;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }



    public Timestamp getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }
}


