package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AbstractInfo {
    @SerializedName("enwiki")
    @Expose
    private String enwiki;

    @SerializedName("baidu")
    @Expose
    private String baidu;

    @SerializedName("zhwiki")
    @Expose
    private String zhwiki;

    @SerializedName("COVID")
    @Expose
    private Covid covid;

    public AbstractInfo(String enwiki, String baidu, String zhwiki, Covid covid) {
        this.enwiki = enwiki;
        this.baidu = baidu;
        this.zhwiki = zhwiki;
        this.covid = covid;
    }

    public String getEnwiki() {
        return enwiki;
    }

    public String getBaidu() {
        return baidu;
    }

    public String getZhwiki() {
        return zhwiki;
    }

    public Covid getCovid() {
        return covid;
    }
}
