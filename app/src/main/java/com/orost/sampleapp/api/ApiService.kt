package com.orost.sampleapp.api

import kotlinx.coroutines.Deferred
import retrofit2.http.*

internal interface ApiService {

    @GET("usersettings/v1")
    fun getUserSettings(): Deferred<Any>

}