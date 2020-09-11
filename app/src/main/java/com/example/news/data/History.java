package com.example.news.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.example.news.models.Data;
import com.example.news.models.Entities;
import com.example.news.models.GeoInfo;
import com.example.news.models.RelatedEvents;

import java.util.List;

@Entity(tableName="news_history_table", indices = {@Index(value = {"contentPrimaryId_col"}, unique = true)})
public class History {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "contentPrimaryId_col")
    private String contentPrimaryId;

    private String category;
    private String content;
    private String date;
    private String contentId;
    private String influence;
    private String lang;
    private String segText;
    private String source;
    private String time;
    private String title;
    private String type;
    private String status;

    public History(String contentPrimaryId, String category, String content, String date,
                   String contentId, String influence, String lang, String segText, String source,
                   String time, String title, String type, String status) {
        this.contentPrimaryId = contentPrimaryId;
        this.category = category;
        this.content = content;
        this.date = date;
        this.contentId = contentId;
        this.influence = influence;
        this.lang = lang;
        this.segText = segText;
        this.source = source;
        this.time = time;
        this.title = title;
        this.type = type;
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getContentPrimaryId() {
        return contentPrimaryId;
    }

    public String getCategory() {
        return category;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getContentId() {
        return contentId;
    }

    public String getInfluence() {
        return influence;
    }

    public String getLang() {
        return lang;
    }

    public String getSegText() {
        return segText;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() { return title; }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

}
