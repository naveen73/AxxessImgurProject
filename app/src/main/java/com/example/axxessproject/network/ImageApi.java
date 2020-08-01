package com.example.axxessproject.network;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * This is retrofit api interface definition
 */
public interface ImageApi {

    // SEARCH
    @Headers("Authorization: Client-ID 137cda6b5008a7c")
    @GET("3/gallery/search")
    Call<ImageSearchResponse> searchImages(@Query("q") String query, @Query("page") String page);
}
