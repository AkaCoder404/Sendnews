package com.example.news.models;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Covid {
    @SerializedName("properties")
    @Expose
    private CovidProperties property;

    @SerializedName("relations")
    @Expose
    private List<Relations> relationsList;

    public Covid(CovidProperties property, List<Relations> relationsList) {
        this.property = property;
        this.relationsList = relationsList;
    }

    public CovidProperties getProperty() {
        return property;
    }

    public List<Relations> getRelationsList() {
        return relationsList;
    }
}
