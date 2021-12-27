package com.suho.demo.githubsearchsample.remote.response

import retrofit2.Response
import java.io.IOException

/*
Api 응답 상태를 담는 래퍼 클래스입니다.
 */
sealed class ApiResponse<out T : Any> {
    data class Success<out T : Any>(val data: T?) : ApiResponse<T>()
    data class Error(val message: String) : ApiResponse<Nothing>()
}

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): ApiResponse<T> {
    return try {
        val response = call.invoke()

        if (response.isSuccessful) {
            ApiResponse.Success(response.body())
        } else {
            ApiResponse.Error(response.message() ?: "")
        }
    } catch (e: IOException) {
        ApiResponse.Error(e.message ?: "")
    }
}
