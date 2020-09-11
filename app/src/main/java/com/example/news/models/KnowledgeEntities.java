package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class KnowledgeEntities {
    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("msg")
    @Expose
    private String msg;

    @SerializedName("data")
    @Expose
    private List<KnowledgeEntity> entityList;

    public KnowledgeEntities(String code, String msg, List<KnowledgeEntity> entityList) {
        this.code = code;
        this.msg = msg;
        this.entityList = entityList;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public List<KnowledgeEntity> getEntityList() {
        return entityList;
    }
}
