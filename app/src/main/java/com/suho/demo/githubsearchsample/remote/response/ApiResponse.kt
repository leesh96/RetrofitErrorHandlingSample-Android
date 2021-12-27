package com.suho.demo.githubsearchsample.remote.response

import java.io.IOException

/*
Api 응답 상태를 담는 래퍼 클래스입니다.
 */
sealed class ApiResponse<out T: Any, out U: Any> {

    data class Success<T : Any>(val data: T) : ApiResponse<T, Nothing>()

    object EmptySuccess : ApiResponse<Nothing, Nothing>()

    // HttpError
    data class ApiError<U : Any>(val error: U, val code: Int) : ApiResponse<Nothing, U>()

    data class NetworkError(val error: IOException) : ApiResponse<Nothing, Nothing>()

    data class UnexpectedError(val error: Throwable?) : ApiResponse<Nothing, Nothing>()
}
