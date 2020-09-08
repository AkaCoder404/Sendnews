package com.example.news.api;

import com.example.news.models.Article;
import com.example.news.models.ArticleContent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface NewsDetailApiInterface {
    @GET("{id}")
    Call<ArticleContent> getArticleContent(@Path("id") String postId);
}



