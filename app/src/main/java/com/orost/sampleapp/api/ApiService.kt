package com.orost.sampleapp.api

import com.orost.sampleapp.model.RedditNews
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top.json")
    fun getNewsAsync(@Query("after") after: String,
                     @Query("limit") limit: Int): Deferred<RedditNews>
}