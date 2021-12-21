package com.suho.demo.githubsearchsample.remote

import com.suho.demo.githubsearchsample.remote.dto.GithubSearchResponse
import com.suho.demo.githubsearchsample.remote.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException

/*
깃허브 리포지토리 검색 API 호출을 처리하는 부분입니다.
 */
object RemoteDataSource {

    private val mClient = NetworkModule.retrofitClient

    suspend fun searchRepositories(query: String): Flow<ApiResponse<GithubSearchResponse>> = flow {
        val response: Response<GithubSearchResponse>
        try {
            response = mClient.searchRepositories(query)
            if (response.isSuccessful) {
                emit(ApiResponse.Success(response.body()!!))
            } else {
                emit(ApiResponse.Error(response.message()))
            }
        } catch (e: IOException) {
            e.printStackTrace()
            emit(ApiResponse.Error(e.toString()))
        }
    }
}