package com.suho.demo.githubsearchsample.remote.response

/*
Api 응답 상태를 담는 래퍼 클래스입니다.
 */
sealed class ApiResponse<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : ApiResponse<T>(data)
    class Error<T>(message: String?, data: T? = null) : ApiResponse<T>(data, message)
}
