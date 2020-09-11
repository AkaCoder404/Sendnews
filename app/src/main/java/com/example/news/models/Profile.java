package com.example.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class Profile {

    @SerializedName("address")
    @Expose
    private String address;

    @SerializedName("affiliation")
    @Expose
    private String affiliation;

    @SerializedName("affiliation_zh")
    @Expose
    private String affiliation_zh;

    @SerializedName("bio")
    @Expose
    private String bio;

    @SerializedName("edu")
    @Expose
    private String edu;

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("email_cr")
    @Expose
    private String email_cr;

    @SerializedName("emails_u")
    @Expose
    private List<Email> email_u;

    @SerializedName("fax")
    @Expose
    private String fax;

    @SerializedName("homepage")
    @Expose
    private String homepage;

    @SerializedName("note")
    @Expose
    private String note;

    @SerializedName("phone")
    @Expose
    private String phone;

    @SerializedName("position")
    @Expose
    private String position;

    @SerializedName("work")
    @Expose
    private String work;

    public Profile(String address, String affiliation, String affiliation_zh, String bio, String email,
                   String edu, String email_cr, List<Email> email_u, String fax, String homepage,
                   String note, String phone, String position, String work) {
        this.address = address;
        this.affiliation = affiliation;
        this.affiliation_zh = affiliation_zh;
        this.bio = bio;
        this.edu = edu;
        this.email = email;
        this.email_cr = email_cr;
        this.email_u = email_u;
        this.fax = fax;
        this.homepage = homepage;
        this.note = note;
        this.phone = phone;
        this.position = position;
        this.work = work;
    }

    public String getAddress() {
        return address;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public String getAffiliation_zh() {
        return affiliation_zh;
    }

    public String getBio() {
        return bio;
    }

    public String getEdu() {
        return edu;
    }

    public String getEmail() {return email; }

    public String getEmail_cr() {
        return email_cr;
    }

    public List<Email> getEmail_u() {
        return email_u;
    }

    public String getFax() {
        return fax;
    }

    public String getHomepage() {
        return homepage;
    }

    public String getNote() {
        return note;
    }

    public String getPhone() {
        return phone;
    }

    public String getPosition() {
        return position;
    }

    public String getWork() {
        return work;
    }
}
