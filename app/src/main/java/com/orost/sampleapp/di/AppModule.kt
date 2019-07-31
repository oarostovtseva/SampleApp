package com.orost.sampleapp.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.utils.BASE_ADDRESS
import com.orost.sampleapp.utils.CoroutineContextProvider
import com.squareup.moshi.Moshi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single { buildApiService(BASE_ADDRESS) }

    single { CoroutineContextProvider() }

}

private fun buildApiService(baseUrl: String): ApiService {
    val loggingInterceptor = (HttpLoggingInterceptor())
        .apply { level = HttpLoggingInterceptor.Level.BODY }
    val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
    val moshi = Moshi.Builder()
        .build()
    return Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()
        .create(ApiService::class.java)
}