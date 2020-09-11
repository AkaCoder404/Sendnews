package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Email {
    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("src")
    @Expose
    private String src;

    @SerializedName("weight")
    @Expose
    private String weight;

    public Email(String address, String src, String weight) {
        this.address = address;
        this.src = src;
        this.weight = weight;
    }

    public String getAddress() {
        return address;
    }

    public String getSrc() {
        return src;
    }

    public String getWeight() {
        return weight;
    }
}
