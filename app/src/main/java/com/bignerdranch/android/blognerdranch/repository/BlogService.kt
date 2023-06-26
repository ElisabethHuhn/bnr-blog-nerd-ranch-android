package com.bignerdranch.android.blognerdranch.repository

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface BlogService {

    @GET("post-metadata")
    fun getPostMetadata(): Call<List<PostMetadata>>

    @GET("post/{id}")
    fun getPost(@Path("id") id: Int): Call<Post>
}