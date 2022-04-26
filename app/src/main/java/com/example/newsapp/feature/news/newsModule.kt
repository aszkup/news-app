package com.example.newsapp.feature.news

import com.example.newsapp.feature.news.ui.NewsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val newsModule = module {

    viewModel { parameters -> NewsViewModel(feedItem = parameters.get()) }
}
