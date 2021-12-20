package com.suho.demo.githubsearchsample.remote

import com.suho.demo.githubsearchsample.remote.dto.GithubSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubService {

    @GET("/search/repositories")
    suspend fun searchRepositories(
        @Query("q") query: String,
        @Query("sort") sort: String = "stars"
    ): Response<GithubSearchResponse>
}