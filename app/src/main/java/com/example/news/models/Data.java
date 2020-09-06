package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.json.JSONObject;

import java.util.List;

import retrofit2.http.Url;

public class Data extends JSONObject {
    @SerializedName("_id")
    @Expose
    private String _id;

    @SerializedName("category")
    @Expose
    private String category;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("entities")
    @Expose
    private List<Entities> entitiesList;

    @SerializedName("geoInfo")
    @Expose
    private List<GeoInfo> geoInfoList;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("influence")
    @Expose
    private String influence;

    @SerializedName("lang")
    @Expose
    private String language;

    @SerializedName("related_events")
    @Expose
    private List<RelatedEvents> relatedEventsList;

    @SerializedName("seg_text")
    @Expose
    private String seg_text;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("tflag")
    @Expose
    private String tflag;



    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("urls")
    @Expose
    private List<String> urlsList;

    public Data(String _id, String category, String content, String date, List<Entities> entitiesList,
                List<GeoInfo> geoInfoList, String id, String influence, String language,
                List<RelatedEvents> relatedEventsList, String seg_text, String source, String tflag, String time
            , String title, String type, List<String> urlsList) {
        this._id = _id;
        this.category = category;
        this.content = content;
        this.date = date;
        this.entitiesList = entitiesList;
        this.geoInfoList = geoInfoList;
        this.id = id;
        this.influence = influence;
        this.language = language;
        this.relatedEventsList = relatedEventsList;
        this.seg_text = seg_text;
        this.source = source;
        this.tflag = tflag;
        this.time = time;
        this.title = title;
        this.type = type;
        this.urlsList = urlsList;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Entities> getEntitiesList() {
        return entitiesList;
    }

    public void setEntitiesList(List<Entities> entitiesList) {
        this.entitiesList = entitiesList;
    }

    public List<GeoInfo> getGeoInfoList() {
        return geoInfoList;
    }

    public void setGeoInfoList(List<GeoInfo> geoInfoList) {
        this.geoInfoList = geoInfoList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getInfluence() {
        return influence;
    }

    public void setInfluence(String influence) {
        this.influence = influence;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<RelatedEvents> getRelatedEventsList() {
        return relatedEventsList;
    }

    public void setRelatedEventsList(List<RelatedEvents> relatedEventsList) {
        this.relatedEventsList = relatedEventsList;
    }

    public String getSeg_text() {
        return seg_text;
    }

    public void setSeg_text(String seg_text) {
        this.seg_text = seg_text;
    }

    public String getTflag() {
        return tflag;
    }

    public void setTflag(String tflag) {
        this.tflag = tflag;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<String> getUrlsList() {
        return urlsList;
    }

    public void setUrlsList(List<String> urlsList) {
        this.urlsList = urlsList;
    }

    public String getContent() {
        return content;
    }

    public void getContent(String content) {
        this.content = content;
    }

    public String getSource() { return source; }
    public String getTitle() {return title; }
}
