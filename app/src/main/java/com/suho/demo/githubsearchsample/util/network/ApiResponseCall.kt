package com.suho.demo.githubsearchsample.util.network

import com.suho.demo.githubsearchsample.remote.response.ApiResponse
import okhttp3.Request
import okhttp3.ResponseBody
import okio.Timeout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

internal class ApiResponseCall<S : Any, E : Any>(
    private val delegate: Call<S>,
    private val errorConverter: Converter<ResponseBody, E>
) : Call<ApiResponse<S, E>> {

    override fun enqueue(callback: Callback<ApiResponse<S, E>>) {
        return delegate.enqueue(object : Callback<S> {
            override fun onResponse(call: Call<S>, response: Response<S>) {
                val body = response.body()
                val code = response.code()
                val error = response.errorBody()

                if (response.isSuccessful) {
                    if (body != null) {
                        callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.Success(body)))
                    } else {
                        callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.EmptySuccess))
                    }
                } else {
                    val errorBody = when {
                        error == null -> null
                        error.contentLength() == 0L -> null
                        else -> try {
                            errorConverter.convert(error)
                        } catch (ex: Exception) {
                            null
                        }
                    }

                    if (errorBody != null) {
                        callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.ApiError(errorBody, code)))
                    } else {
                        callback.onResponse(this@ApiResponseCall, Response.success(ApiResponse.UnexpectedError(null)))
                    }
                }
            }

            override fun onFailure(call: Call<S>, t: Throwable) {
                val failureResponse = when (t) {
                    is IOException -> ApiResponse.NetworkError(t)
                    else -> ApiResponse.UnexpectedError(t)
                }
                callback.onResponse(this@ApiResponseCall, Response.success(failureResponse))
            }

        })
    }

    override fun clone() = ApiResponseCall(delegate.clone(), errorConverter)

    override fun execute(): Response<ApiResponse<S, E>> {
        throw UnsupportedOperationException()
    }

    override fun isExecuted() = delegate.isExecuted

    override fun cancel() = delegate.cancel()

    override fun isCanceled() = delegate.isCanceled

    override fun request(): Request = delegate.request()

    override fun timeout(): Timeout = delegate.timeout()
}