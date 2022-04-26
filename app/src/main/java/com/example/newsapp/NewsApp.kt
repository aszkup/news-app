package com.example.newsapp

import android.app.Application
import android.content.Context
import com.example.newsapp.data.apiModule
import com.example.newsapp.data.networkModule
import com.example.newsapp.feature.feed.feedModule
import com.example.newsapp.feature.news.newsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class NewsApp : Application() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        setupKoin()
    }

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setupKoin() {
        startKoin {
            androidLogger()
            androidContext(this@NewsApp)

            koin.loadModules(appModules)
        }
    }
}

val appModules = mutableListOf(networkModule, apiModule, feedModule, newsModule)
