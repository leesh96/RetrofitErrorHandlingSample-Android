package com.suho.demo.githubsearchsample.util.network

import com.suho.demo.githubsearchsample.remote.response.ApiResponse
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {

        if (Call::class.java != getRawType(returnType)) {
            return null
        }

        check(returnType is ParameterizedType) {
            "return type must be parameterized as Call<ApiResponse<T>>"
        }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != ApiResponse::class.java) {
            return null
        }

        check(responseType is ParameterizedType) {
            "response must be parameterized as ApiResponse<T>"
        }

        val successBodyType = getParameterUpperBound(0, responseType)
        val errorBodyType = getParameterUpperBound(1, responseType)

        val errorBodyConverter = retrofit.nextResponseBodyConverter<Any>(null, errorBodyType, annotations)

        return ApiResponseAdapter<Any, Any>(successBodyType, errorBodyConverter)
    }

}