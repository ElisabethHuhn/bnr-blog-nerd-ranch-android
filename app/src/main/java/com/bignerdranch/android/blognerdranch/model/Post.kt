package com.bignerdranch.android.blognerdranch.model

data class Post (
    var id: Int? = null,
    var metadata: PostMetadata? = null,
    var body: String? = null,
)