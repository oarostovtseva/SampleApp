package com.orost.sampleapp.model

import com.squareup.moshi.Json

data class RedditNews(
        var data: RedditData
)

data class RedditData(
        var after: String,
        var before: String,
        var children: List<RedditChildren>
)

data class RedditChildren(
        var data: RedditNewsData
)

data class RedditNewsData(
        var author: String,
        var title: String,
        @Json(name = "num_comments")
        var numComments: Int,
        var created: Long,
        var thumbnail: String,
        var url: String
)


