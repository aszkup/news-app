package com.example.newsapp.data

import android.content.Context
import com.example.newsapp.BuildConfig
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File

private const val DISK_CACHE_SIZE = 10 * 1024 * 1024

val networkModule = module {

    single<Retrofit> { provideRetrofit(okHttpClient = get()) }
    single { provideHttpLoggingInterceptor() }
    single { AuthAddingInterceptor() }
    single { provideHttpCacheDir(context = get()) }
    single { provideOkHttpClient(loggingInterceptor = get(), authAddingInterceptor = get(), cacheDir = get()) }
}

private fun provideRetrofit(okHttpClient: OkHttpClient) = Retrofit.Builder().apply {
    baseUrl(BuildConfig.BACKEND_URL)
    addConverterFactory(GsonConverterFactory.create())
    client(okHttpClient)
}.build()

private fun provideHttpLoggingInterceptor() = HttpLoggingInterceptor { message -> Timber.d(message) }.apply {
    level = HttpLoggingInterceptor.Level.BODY
}

private fun provideOkHttpClient(
    loggingInterceptor: HttpLoggingInterceptor,
    authAddingInterceptor: AuthAddingInterceptor,
    cacheDir: File
) = OkHttpClient.Builder().apply {
    addInterceptor(authAddingInterceptor)
    addInterceptor(loggingInterceptor)
    cache(Cache(cacheDir, DISK_CACHE_SIZE.toLong()))
}.build()

private fun provideHttpCacheDir(context: Context) = File(context.cacheDir, "http")
