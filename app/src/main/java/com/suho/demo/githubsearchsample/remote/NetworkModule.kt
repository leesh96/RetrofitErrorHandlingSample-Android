package com.suho.demo.githubsearchsample.remote

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkModule {

    private const val BASE_URL = "https://api.github.com"

    private val mGson = GsonBuilder().create()

    private val mHttpClient = OkHttpClient.Builder().build()

    private val mRetrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(mHttpClient)
        .addConverterFactory(GsonConverterFactory.create(mGson))
        .build()

    val retrofitClient: GithubService = mRetrofit.create(GithubService::class.java)
}