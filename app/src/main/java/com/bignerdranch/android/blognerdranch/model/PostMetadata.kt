package com.bignerdranch.android.blognerdranch.model

data class PostMetadata (
    var postId: Int? = null,
    var title: String? = null,
    var summary: String? = null,
    var author: Author? = null,
    var publishDate: String? = null,
)