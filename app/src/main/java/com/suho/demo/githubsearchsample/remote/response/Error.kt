package com.suho.demo.githubsearchsample.remote.response

import com.google.gson.annotations.SerializedName

data class Error(

    @SerializedName("status")
    val status: String,

    @SerializedName("message")
    val message: String
)
