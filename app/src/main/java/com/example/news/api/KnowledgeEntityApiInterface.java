package com.example.news.api;

import com.example.news.models.KnowledgeEntities;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface KnowledgeEntityApiInterface {
    @GET("entityquery")
    Call<KnowledgeEntities> getEntities(
            @Query("entity") String entity);
}
