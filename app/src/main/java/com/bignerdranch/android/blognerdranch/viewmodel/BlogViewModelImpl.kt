package com.bignerdranch.android.blognerdranch.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.repository.BlogRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BlogViewModelImpl : ViewModel()
{
    private val TAG = "BlogViewModel: "

    private var repo : BlogRepositoryImpl = BlogRepositoryImpl()

    val defaultPost = Post()
    private val defaultPostMetadata = PostMetadata()
    val postFlow : MutableStateFlow<Post> = MutableStateFlow(defaultPost)
    val postsFlow : MutableStateFlow<List<PostMetadata>> = MutableStateFlow(listOf( defaultPostMetadata))

//    init {
        // Initialize the  data.
//        fetchPosts()
//    }

    //Calls to Retrofit
    fun fetchPosts()  {
        repo.fetchPosts(
            postsCallbackHandler = postsCallbackHandler
        )
    }

    fun fetchPost(id: Int)  {
        repo.fetchPost(
            id = id,
            postCallbackHandler = postCallbackHandler
        )
    }


    //Create the callback objects that will parse the response and
    //  define the code to execute upon request return

    //This callback is in response to fetchPosts (plural) and will trigger displaying the List Pane
    private val postsCallbackHandler = object: Callback<List<PostMetadata>?> {
        override fun onFailure(call: Call<List<PostMetadata>?>, t: Throwable) {
            Log.e(TAG, "Failed to load postMetadata", t)
        }

        override fun onResponse(call: Call<List<PostMetadata>?>, response: Response<List<PostMetadata>?>) {
            Log.i(TAG, "Loaded postMetadata $response")
            returnPostsToViewModel(response.body() ?: listOf())
//            updatePosts( response.body() ?: listOf())
        }
    }

    //This callback is in response to fetchPost (singular) and will trigger displaying the Detail Pane
    private val postCallbackHandler = object: Callback<Post?> {
        override fun onFailure(call: Call<Post?>, t: Throwable) {
            Log.e(TAG, "Failed to load post", t)
        }

        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
            Log.i(TAG, "Loaded post $response")
            returnPostToViewModel(response.body() ?: defaultPost)
//            updateCurrentPost(response.body())
            //updating the post will trigger the LiveData observer to navigate to the detail pane
        }
    }

    fun returnPostsToViewModel(posts: List<PostMetadata>){
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            postsFlow.emit(posts)
        }
    }

    fun returnPostToViewModel(post: Post){
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            postFlow.emit(post)
        }
    }
}