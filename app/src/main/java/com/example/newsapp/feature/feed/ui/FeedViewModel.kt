package com.example.newsapp.feature.feed.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.feature.feed.FeedItemMapper
import com.example.newsapp.feature.feed.GetFeedUseCase
import com.example.newsapp.feature.feed.PAGE_SIZE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.Serializable

class FeedViewModel(
    private val getFeedUseCase: GetFeedUseCase,
    private val feedItemMapper: FeedItemMapper
) : ViewModel() {

    var lastPosition: Int = 0
    val isRefreshing = MutableLiveData(false)
    private val isLoading = MutableLiveData(false)
    val isErrorVisible = MutableLiveData(false)
    val items = MutableLiveData<List<FeedItem>>()
    var currentPage = 0

    init {
        loadFeed(page = 1)
    }

    private fun loadFeed(page: Int) {
        isLoading.postValue(true)
        isErrorVisible.postValue(false)
        viewModelScope.launch {
            getFeedUseCase(page = page)
                .map { feedItemMapper.map(it, page, PAGE_SIZE) }
                .flowOn(Dispatchers.IO)
                .catch {
                    isLoading.postValue(false)
                    isRefreshing.postValue(false)
                    isErrorVisible.postValue(true)
                    Timber.e(it)
                }
                .collect {
                    currentPage = page
                    isErrorVisible.postValue(false)
                    isLoading.postValue(false)
                    isRefreshing.postValue(false)
                    val currentItems = items.value?.toMutableList() ?: mutableListOf()
                    Timber.d("Ids: ${it.map { it.localId }}")
                    currentItems.addAll(it)
                    items.postValue(currentItems)
                }
        }
    }

    fun refresh() {
        if (isLoading.value == true) return
        isRefreshing.postValue(true)
        items.postValue(listOf())
        loadFeed(page = 1)
    }

    fun loadNextPage() {
        if (isLoading.value == true) return
        loadFeed(page = currentPage + 1)
    }
}

data class FeedItem(
    val localId: Long,
    val source: String,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
) : Serializable
