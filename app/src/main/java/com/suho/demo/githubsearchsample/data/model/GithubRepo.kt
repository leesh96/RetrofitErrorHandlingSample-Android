package com.suho.demo.githubsearchsample.data.model

/*
View에 표시될 모델입니다.
 */
data class GithubRepo(

    val id: Int,
    val name: String,
    val ownerNickname: String,
    val ownerAvatar: String,
    val stars: Int,
    val forks: Int,
    val issues: Int,
    val watchers: Int
)
