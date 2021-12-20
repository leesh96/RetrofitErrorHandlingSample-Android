package com.suho.demo.githubsearchsample.remote.dto

import com.google.gson.annotations.SerializedName

data class RepositoryDetailResponse(

    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("stargazers_count")
    val starts: Int,

    @SerializedName("watchers_count")
    val watchers: Int,

    @SerializedName("forks_count")
    val forks: Int,

    @SerializedName("open_issues_count")
    val issues: Int,

    @SerializedName("owner")
    val owner: RepositoryOwnerResponse
)

data class RepositoryOwnerResponse(

    @SerializedName("login")
    val nickname: String,

    @SerializedName("avatar_url")
    val avatar: String
)
