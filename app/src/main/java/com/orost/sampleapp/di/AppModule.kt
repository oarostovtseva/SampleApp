package com.orost.sampleapp.di

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.orost.sampleapp.api.ApiService
import com.orost.sampleapp.ui.NewsFragment
import com.orost.sampleapp.utils.BASE_ADDRESS
import com.orost.sampleapp.utils.CoroutineContextProvider
import com.orost.sampleapp.viewmodel.NewsViewModel
import com.squareup.moshi.Moshi
import com.squareup.picasso.OkHttp3Downloader
import com.squareup.picasso.Picasso
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val appModule = module {

    single { createOkHttpClient() }
    single { buildApiService(BASE_ADDRESS, createOkHttpClient()) }
    single { CoroutineContextProvider() }

    single { initPicasso(get(), get()) }

    factory { NewsFragment() }

    viewModel { NewsViewModel(get(), get()) }

}

private fun createOkHttpClient(): OkHttpClient {
    val loggingInterceptor = HttpLoggingInterceptor()
        .apply { level = HttpLoggingInterceptor.Level.BODY }
    return OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()
}

private fun initPicasso(okHttpClient: OkHttpClient, context: Context): Picasso =
    Picasso.Builder(context).downloader(OkHttp3Downloader(okHttpClient)).build()

private fun buildApiService(baseUrl: String, okHttpClient: OkHttpClient): ApiService {
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