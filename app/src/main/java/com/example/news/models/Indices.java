package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Indices {
    @SerializedName("activity")
    @Expose
    private String activity;

    @SerializedName("citations")
    @Expose
    private String citations;

    @SerializedName("diversity")
    @Expose
    private String diversity;

    @SerializedName("gindex")
    @Expose
    private String gindex;

    @SerializedName("hindex")
    @Expose
    private String hindex;

    @SerializedName("newStar")
    @Expose
    private String newStar;

    @SerializedName("pubs")
    @Expose
    private String pubs;

    @SerializedName("risingStar")
    @Expose
    private String risingStar;

    @SerializedName("sociability")
    @Expose
    private String sociability;

    public Indices(String activity, String citations, String diversity, String gindex,
                   String hindex, String newStar, String pubs, String risingStar,
                   String sociability) {
        this.activity = activity;
        this.citations = citations;
        this.diversity = diversity;
        this.gindex = gindex;
        this.hindex = hindex;
        this.newStar = newStar;
        this.pubs = pubs;
        this.risingStar = risingStar;
        this.sociability = sociability;
    }

    public String getActivity() {
        return activity;
    }

    public String getCitations() {
        return citations;
    }

    public String getDiversity() {
        return diversity;
    }

    public String getGindex() {
        return gindex;
    }

    public String getHindex() {
        return hindex;
    }

    public String getNewStar() {
        return newStar;
    }

    public String getPubs() {
        return pubs;
    }

    public String getRisingStar() {
        return risingStar;
    }

    public String getSociability() {
        return sociability;
    }
}
