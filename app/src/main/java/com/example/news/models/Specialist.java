package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Specialist {
//    @SerializedName("aff")
//    @Expose
//    private List<String> aff;

    @SerializedName("avatar")
    @Expose
    private String avatar;

    @SerializedName("bind")
    @Expose
    private String bind;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("indices")
    @Expose
    private Indices indices;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("name_zh")
    @Expose
    private String name_zh;

    @SerializedName("num_followed")
    @Expose
    private String num_followed;

    @SerializedName("num_viewed")
    @Expose
    private String num_viewed;

    @SerializedName("profile")
    @Expose
    private Profile profile;

    @SerializedName("score")
    @Expose
    private String score;

    @SerializedName("sourcetype")
    @Expose
    private String sourcetype;

    @SerializedName("tags")
    @Expose
    private List<String> tags;

    @SerializedName("tags_score")
    @Expose
    private List<String> tags_score;

    @SerializedName("index")
    @Expose
    private String index;

    @SerializedName("tab")
    @Expose
    private String tab;

    @SerializedName("is_passedaway")
    @Expose
    private String is_passedaway;

    public Specialist(/*List<String> aff,*/ String avatar, String bind, String id, Indices indices,
                      String name, String name_zh, String num_followed, String num_viewed, Profile profile,
                      String source, String sourcetype, List<String> tags,
                      List<String> tags_score, String index, String tab, String is_passedaway) {
        //this.aff = aff;
        this.avatar = avatar;
        this.bind = bind;
        this.id = id;
        this.indices = indices;
        this.name = name;
        this.name_zh = name_zh;
        this.num_followed = num_followed;
        this.num_viewed = num_viewed;
        this.profile = profile;
        this.score = source;
        this.sourcetype = sourcetype;
        this.tags = tags;
        this.tags_score = tags_score;
        this.index = index;
        this.tab = tab;
        this.is_passedaway = is_passedaway;
    }

//    public List<String> getAffiliation() {
//        return aff;
//    }

    public String getAvatar() {
        return avatar;
    }

    public String getBind() {
        return bind;
    }

    public String getId() {
        return id;
    }

    public Indices getIndices() {
        return indices;
    }

    public String getName() {
        return name;
    }

    public String getName_zh() {
        return name_zh;
    }

    public String getNum_followed() {
        return num_followed;
    }

    public String getNum_viewed() {
        return num_viewed;
    }

    public Profile getProfile() {
        return profile;
    }

    public String getSource() {
        return score;
    }

    public String getSourcetype() {
        return sourcetype;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<String> getTags_score() {
        return tags_score;
    }

    public String getIndex() {
        return index;
    }

    public String getTab() {
        return tab;
    }

    public String getIs_passedaway() {
        return is_passedaway;
    }
}
