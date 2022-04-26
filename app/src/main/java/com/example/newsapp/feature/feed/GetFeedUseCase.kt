package com.example.newsapp.feature.feed

import com.example.newsapp.data.NewsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val PAGE_SIZE = 20

class GetFeedUseCase(
    private val newsApi: NewsApi
) {

    suspend operator fun invoke(page: Int): Flow<FeedDto> = flow {
        val response = newsApi.getFeed(query = "bitcoin", page = page, pageSize = PAGE_SIZE)
        emit(response)
    }
}

data class FeedDto(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsDto>
)

data class SourceDto(
    val id: String?,
    val name: String
)

data class NewsDto(
    val source: SourceDto,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String,
)
