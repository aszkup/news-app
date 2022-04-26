package com.example.newsapp.feature.feed

import com.example.newsapp.feature.feed.ui.FeedItem

class FeedItemMapper {

    suspend fun map(feedDto: FeedDto, page: Int, pageSize: Int): List<FeedItem> {
        return feedDto.articles.mapIndexed { index, dto ->
            FeedItem(
                (((page - 1) * pageSize) + index).toLong(),
                dto.source.name,
                dto.author,
                dto.title,
                dto.description,
                dto.url,
                dto.urlToImage,
                dto.publishedAt,
                dto.content
            )
        }
    }
}
