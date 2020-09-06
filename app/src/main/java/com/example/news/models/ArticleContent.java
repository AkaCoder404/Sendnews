package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

public class ArticleContent {

    @SerializedName("data")
    @Expose
    private Data data;

    @SerializedName("status")
    @Expose
    private String status;

    public ArticleContent(Data data, String status) {
        this.data = data;
        this.status = status;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
