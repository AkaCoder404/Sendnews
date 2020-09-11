package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Relations {
    @SerializedName("relation")
    @Expose
    private String relation;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("label")
    @Expose
    private String label;

    @SerializedName("foward")
    @Expose
    private String foward;

    public Relations(String relation, String url, String label, String foward) {
        this.relation = relation;
        this.url = url;
        this.label = label;
        this.foward = foward;
    }

    public String getRelation() {
        return relation;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public String getFoward() {
        return foward;
    }
}
