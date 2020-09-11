package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Specialists {
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("message")
    @Expose
    private String message;

    @SerializedName("data")
    @Expose
    private List<Specialist> specialistList;

    public Specialists(String status, String message, List<Specialist> specialistList) {
        this.status = status;
        this.message = message;
        this.specialistList = specialistList;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public List<Specialist> getSpecialistList() {
        return specialistList;
    }
}
