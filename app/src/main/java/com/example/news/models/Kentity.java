package com.example.news.models;


import java.util.HashMap;

public class Kentity {

    private String hot;

    private String label;

    private String url;

    private HashMap<String, String> prop;

    private String img;

    public Kentity(String hot, String label, String url, HashMap<String, String> prop, String img) {
        this.hot = hot;
        this.label = label;
        this.url = url;
        this.prop = prop;
        this.img = img;
    }
}
