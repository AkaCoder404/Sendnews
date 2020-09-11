package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class KnowledgeEntity {
    @SerializedName("hot")
    @Expose
    private String hot;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("abstractInfo")
    @Expose
    private AbstractInfo abstractInfo;

    @SerializedName("img")
    @Expose
    private String img;

    public KnowledgeEntity(String hot, String label, String url, AbstractInfo abstractInfo, String img) {
        this.hot = hot;
        this.label = label;
        this.url = url;
        this.abstractInfo = abstractInfo;
        this.img = img;
    }

    public String getHot() {
        return hot;
    }

    public String getLabel() {
        return label;
    }

    public String getUrl() {
        return url;
    }

    public AbstractInfo getAbstractInfo() {
        return abstractInfo;
    }

    public String getImg() {
        return img;
    }
}
