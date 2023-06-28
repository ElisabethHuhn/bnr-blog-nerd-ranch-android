package com.bignerdranch.android.blognerdranch.repository

import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Callback

interface BlogRepository {
    fun fetchPost(id: Int, postCallbackHandler: Callback<Post?>)
    fun fetchPosts(postsCallbackHandler: Callback<List<PostMetadata>?>)
}


class BlogRepositoryImpl : BlogRepository {
    //remote data source variables
    private val blogApi: BlogService

    init {
        /*
         * Use RetrofitHelper to create the instance of Retrofit
         * Then use this instance to create an instance of the API
         */
        blogApi = RetrofitHelper.getInstance().create(BlogService::class.java)
    }

     override fun fetchPost(id: Int, postCallbackHandler: Callback<Post?>)
      {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val postCall = blogApi.getPost(id = id)

            //Initiate the remote call, with the passed callback to handle the remote response
            postCall.enqueue(postCallbackHandler)
        }
    }

    override fun fetchPosts(postsCallbackHandler: Callback<List<PostMetadata>?>)
    {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val postCall = blogApi.getPostMetadata()

            //Initiate the remote call, with the passed callback to handle the remote response
            postCall.enqueue(postsCallbackHandler)
        }
    }
}