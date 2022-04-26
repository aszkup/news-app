package com.example.newsapp.data

import okhttp3.Interceptor
import okhttp3.Response

private const val AUTH_TOKEN = "f58cb1c27fe24baab4940d6275e6b3c4"
private const val AUTH_HEADER = "Authorization"

class AuthAddingInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .removeHeader(AUTH_HEADER)
            .header(AUTH_HEADER, AUTH_TOKEN)
            .build()
        return chain.proceed(request)
    }
}
