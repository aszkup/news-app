package com.example.newsapp.feature.feed

import com.example.newsapp.feature.feed.ui.FeedViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val feedModule = module {

    factory { GetFeedUseCase(newsApi = get()) }
    factory { FeedItemMapper() }

    viewModel { FeedViewModel(getFeedUseCase = get(), feedItemMapper = get()) }
}
