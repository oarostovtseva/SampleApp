package com.orost.sampleapp.api

import com.orost.sampleapp.model.RedditNews
import kotlinx.coroutines.Deferred
import retrofit2.http.GET

interface ApiService {

    @GET("top.json")
    fun getNewsAsync(): Deferred<RedditNews>
}