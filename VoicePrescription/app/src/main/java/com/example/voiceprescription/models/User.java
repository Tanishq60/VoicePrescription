package com.example.voiceprescription.models;

import androidx.annotation.NonNull;

public class User {
    String name, email, type, photoUrl, phone;
    boolean requestForDoctor;

    public User () {}

    public User(String name, String email, String type, String photoUrl, String phone, boolean requestForDoctor) {
        this.name = name;
        this.email = email;
        this.type = type;
        this.photoUrl = photoUrl;
        this.phone = phone;
        this.requestForDoctor = requestForDoctor;
    }

    public boolean isRequestForDoctor() {
        return requestForDoctor;
    }

    public void setRequestForDoctor(boolean requestForDoctor) {
        this.requestForDoctor = requestForDoctor;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + this.name + ", Email: " + this.email + ", Type: " + this.type +
                "\nPhotoURL: " + this.photoUrl +
                "\nPhone: " + this.phone +
                "\nRequest for becoming doctor: " + this.requestForDoctor;
    }
}


