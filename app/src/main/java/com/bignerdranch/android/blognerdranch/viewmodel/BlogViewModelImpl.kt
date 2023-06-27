package com.bignerdranch.android.blognerdranch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bignerdranch.android.blognerdranch.controller.list.PostListActivity
import com.bignerdranch.android.blognerdranch.controller.post.PostActivity
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
    private var repo : BlogRepositoryImpl = BlogRepositoryImpl()

    private var _postId: MutableLiveData<Int> = MutableLiveData<Int>(0)
    val currentPostId : LiveData<Int>
        get() = _postId

    private var _posts = MutableLiveData<List<PostMetadata>>()
    val posts : LiveData<List<PostMetadata>>
        get() = _posts

    private var _currentPost: MutableLiveData<Post?> = MutableLiveData<Post?>()
    val currentPost: LiveData<Post?>
        get() = _currentPost

    private var _currentPostMeta: MutableLiveData<PostMetadata?> = MutableLiveData<PostMetadata?>()

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
            Log.e(PostListActivity.TAG, "Failed to load postMetadata", t)
        }

        override fun onResponse(call: Call<List<PostMetadata>?>, response: Response<List<PostMetadata>?>) {
            Log.i(PostListActivity.TAG, "Loaded postMetadata $response")
            returnPostsToViewModel(response.body() ?: listOf())
//            updatePosts( response.body() ?: listOf())
        }
    }

    //This callback is in response to fetchPost (singular) and will trigger displaying the Detail Pane
    private val postCallbackHandler = object: Callback<Post?> {
        override fun onFailure(call: Call<Post?>, t: Throwable) {
            Log.e(PostActivity.TAG, "Failed to load post", t)
        }

        override fun onResponse(call: Call<Post?>, response: Response<Post?>) {
            Log.i(PostActivity.TAG, "Loaded post $response")
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