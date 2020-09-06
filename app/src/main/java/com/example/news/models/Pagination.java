package com.example.news.models;

public class Pagination {
    private int id;
    private String message;
    Article article;

    public Pagination(Article articles) {
        this.article = articles;
    }

    public Article getArticle() {
        return this.article;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
