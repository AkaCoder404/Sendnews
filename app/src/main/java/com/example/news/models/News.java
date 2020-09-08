package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class News {
    @SerializedName("tflag")
    @Expose
    private String tflag;

    @SerializedName("datas")
    @Expose
    private List<Article> article;

    public News(String tflag, List<Article> article) {
        this.tflag = tflag;
        this.article = article;
    }

    public String getTflag() {
        return tflag;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }

    public List<Article> getArticle() { return article; }

    public void setArticle(List<Article> article) {
        this.article = article;
    }
}


