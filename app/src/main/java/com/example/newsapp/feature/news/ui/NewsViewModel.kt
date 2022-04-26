package com.example.newsapp.feature.news.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.newsapp.feature.feed.ui.FeedItem

class NewsViewModel(
    feedItem: FeedItem
) : ViewModel() {

    val item: LiveData<FeedItem> = MutableLiveData(feedItem)
}
