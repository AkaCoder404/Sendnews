package com.example.news.api;

import com.example.news.models.News;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("events.json")
    Call<News> getNews();

    //Doesn't Work Since Everything is in One JsonFile
    @GET("events.json")
    Call<News> searchNews(
            @Query("q") String keyword
    );

}
