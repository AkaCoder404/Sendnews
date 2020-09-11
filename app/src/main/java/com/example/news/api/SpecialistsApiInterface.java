package com.example.news.api;

import com.example.news.models.Specialists;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface SpecialistsApiInterface {

    @GET("get_ncov_expers_list")
    Call<Specialists> getExperts(
            @Query("v") String id);


}
