package com.gigfa.ariya.DataLayer

import com.gigfa.ariya.Model.ModelRandomResponse
import com.gigfa.ariya.Model.ModelSearchResponse
import com.gigfa.ariya.Model.ModelTrendingResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    //?api_key={api_key}&limit={limit}&rating{rating}

    @GET("trending")
    //@HTTP(method = "GET", path = "trending", hasBody = true)
    @Headers("accept:application/json", "Content-Type: application/json")
    fun getTrending(
        @Query("api_key") apiKey: String,
        @Query("limit") limit: Int,
        @Query("rating") rating: String,
    ): Call<ModelTrendingResponse>

    @GET("search")
    //@HTTP(method = "GET", path = "trending", hasBody = true)
    @Headers("accept:application/json", "Content-Type: application/json")
    fun getSearch(
        @Query("api_key") apiKey: String,
        @Query("q") q: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String,
        @Query("lang") lang: String,
    ): Call<ModelSearchResponse>

    @GET("random")
    //@HTTP(method = "GET", path = "trending", hasBody = true)
    @Headers("accept:application/json", "Content-Type: application/json")
    fun getRandom(
        @Query("api_key") apiKey: String,
        @Query("tag") tag: String,
        @Query("rating") rating: String,
    ): Call<ModelRandomResponse>

}