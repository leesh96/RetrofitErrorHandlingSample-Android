package com.suho.demo.githubsearchsample.util.network

import com.suho.demo.githubsearchsample.remote.response.ApiResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Converter
import java.lang.reflect.Type

class ApiResponseAdapter<S : Any, E : Any>(
    private val successType: Type,
    private val errorBodyConverter: Converter<ResponseBody, E>
) : CallAdapter<S, Call<ApiResponse<S, E>>> {

    override fun responseType(): Type = successType

    override fun adapt(call: Call<S>): Call<ApiResponse<S, E>> = ApiResponseCall(call, errorBodyConverter)
}