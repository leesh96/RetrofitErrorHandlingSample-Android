package com.suho.demo.githubsearchsample.data.repository

import com.suho.demo.githubsearchsample.data.common.Resource
import com.suho.demo.githubsearchsample.data.model.GithubRepo
import com.suho.demo.githubsearchsample.remote.NetworkModule
import com.suho.demo.githubsearchsample.remote.RemoteDataSource
import com.suho.demo.githubsearchsample.remote.response.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/*
검색결과를 가져오는 리포지토리입니다.
현재는 remote data source 밖에 없어 ApiResponse를 View에서 사용되는 모델로 매핑, Api 응답 상태에 따라 처리 진행
 */
object SearchRepository {

    private val mClient = NetworkModule.retrofitClient

    suspend fun getSearchResult(query: String): Flow<Resource<List<GithubRepo>>> = flow {
        emit(Resource.loading(null))
        when (val apiResponse = mClient.searchRepositories(query)) {
            is ApiResponse.Success -> emit(Resource.success(apiResponse.data.items.map {
                GithubRepo(
                    id = it.id,
                    name = it.name,
                    ownerNickname = it.owner.nickname,
                    ownerAvatar = it.owner.avatar,
                    stars = it.stars,
                    forks = it.forks,
                    issues = it.issues,
                    watchers = it.watchers
                )
            }))
            is ApiResponse.EmptySuccess -> emit(Resource.success(null))
            is ApiResponse.ApiError -> emit(Resource.error(apiResponse.error.message, null))
            is ApiResponse.NetworkError -> emit(Resource.error("network error", null))
            is ApiResponse.UnexpectedError -> emit(Resource.error("fail", null))
        }
    }
}