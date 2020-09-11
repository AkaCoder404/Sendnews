package com.example.news.models;


import androidx.room.Ignore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class Covid {
    @SerializedName("properties")
    @Expose
    private CovidProperties property;


    @SerializedName("relations")
    @Expose
    private List<Relations> relationsList;

    public Covid(CovidProperties property, List<String> properties, List<Relations> relationsList) {
        this.property = property;
        //this.properties = properties;
        this.relationsList = relationsList;
    }

    public CovidProperties getProperty() {
        return property;
    }

    public List<Relations> getRelationsList() {
        return relationsList;
    }

    //public List<String> getProperties() { return properties; }
}
