package com.suho.demo.githubsearchsample.data.repository

import com.suho.demo.githubsearchsample.data.common.Resource
import com.suho.demo.githubsearchsample.data.model.GithubRepo
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

    suspend fun getSearchResult(query: String): Flow<Resource<List<GithubRepo>>> = flow {
        emit(Resource.loading(null))
        RemoteDataSource.searchRepositories(query).collect {
            when(it) {
                is ApiResponse.Success -> {
                    emit(Resource.success(it.data?.items?.map { data ->
                        GithubRepo(
                            id = data.id,
                            name = data.name,
                            ownerNickname = data.owner.nickname,
                            ownerAvatar = data.owner.avatar,
                            stars = data.stars,
                            forks = data.forks,
                            issues = data.issues,
                            watchers = data.watchers
                        )
                    }))
                }
                is ApiResponse.Error -> {
                    emit(Resource.error(it.message ?: "", null))
                }
            }
        }
    }
}