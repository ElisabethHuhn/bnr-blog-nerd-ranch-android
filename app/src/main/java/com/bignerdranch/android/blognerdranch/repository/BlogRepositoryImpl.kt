package com.bignerdranch.android.blognerdranch.repository

import android.util.Log
import com.bignerdranch.android.blognerdranch.controller.post.PostActivity
import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface BlogRepository {
    fun fetchPost(id: Int, postCallbackHandler: Callback<Post?>)
    fun fetchPosts(postsCallbackHandler: Callback<List<PostMetadata>?>)
}


class BlogRepositoryImpl : BlogRepository {
    //remote data source variables
    private val blogApi: BlogService

    lateinit var postsFlow: MutableStateFlow<List<Post>>

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


    //Create the callback object that will parse the response and
    // actually fill the drivers and routes variables
    //Define the code to execute upon request return
    val postCallbackHandler = object: Callback<Post?> {
        override fun onFailure(call: Call<Post?>, t: Throwable) {
            Log.e(PostActivity.TAG, "Failed to load post", t)
        }

        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
            Log.i(PostActivity.TAG, "Loaded post $response")
            val post = response.body() //need to get this back to the ViewModel
//            updateUI(response.body()!!)
        }
    }

//    fun returnBlogsToViewModel(posts: List<Post?>){
//        val scope = CoroutineScope(Dispatchers.IO)
//        scope.launch {
//            postsFlow.emit(posts)
//        }
//    }



}