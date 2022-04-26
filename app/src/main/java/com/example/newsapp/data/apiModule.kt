package com.example.newsapp.data

import org.koin.dsl.module
import retrofit2.Retrofit

val apiModule = module {

    single<NewsApi> { getGalleryApi(retrofit = get()) }
}

fun getGalleryApi(retrofit: Retrofit): NewsApi = retrofit.create(NewsApi::class.java)
