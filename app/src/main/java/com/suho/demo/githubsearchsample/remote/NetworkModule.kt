package com.suho.demo.githubsearchsample.remote

import com.google.gson.GsonBuilder
import com.suho.demo.githubsearchsample.BuildConfig
import com.suho.demo.githubsearchsample.util.network.ApiResponseAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://api.github.com"

    private val mGson = GsonBuilder().create()

    private val logger = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val mHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(logger)
        .build()

    private val mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(mHttpClient)
        .addCallAdapterFactory(ApiResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(mGson))
        .build()

    val retrofitClient: GithubService = mRetrofit.create(GithubService::class.java)
}