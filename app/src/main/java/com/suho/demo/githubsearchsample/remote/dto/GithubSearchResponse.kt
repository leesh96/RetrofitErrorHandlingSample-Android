package com.suho.demo.githubsearchsample.remote.dto

import com.google.gson.annotations.SerializedName

/*
remote에서 넘어오는 데이터를 담기위한 모델입니다.
 */
data class GithubSearchResponse(

    @SerializedName("total_count")
    val totalCount: Int,

    @SerializedName("items")
    val items: List<RepositoryDetailResponse>
)