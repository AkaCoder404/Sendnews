package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Article {
    @SerializedName("_id")
    @Expose
    private String id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("lang")
    @Expose
    private String lang;

    @SerializedName("geoInfo")
    @Expose
    private List<GeoInfo> geoInfo;

    @SerializedName("influence")
    @Expose
    private String influence;

    public Article(String id, String type, String title, String category, String time,
                   String lang, List<GeoInfo> geoInfo, String influence) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.category = category;
        this.time = time;
        this.lang = lang;
        this.geoInfo = geoInfo;
        this.influence = influence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<GeoInfo> getGeoInfo() {
        return geoInfo;
    }

    public void setGeoInfo(List<GeoInfo> geoInfo) {
        this.geoInfo = geoInfo;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }
}
