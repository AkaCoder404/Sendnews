package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeoInfo {
    @SerializedName("originText")
    @Expose
    private String originText;

    @SerializedName("geoName")
    @Expose
    private String geoName;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;
}
