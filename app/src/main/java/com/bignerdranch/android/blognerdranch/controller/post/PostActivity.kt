package com.bignerdranch.android.blognerdranch.controller.post

import android.os.Bundle
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.blognerdranch.R
import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.viewmodel.BlogViewModelImpl

class

PostActivity : AppCompatActivity() {

    private var postId: Int = 0

    private var postTitle: TextView? = null
    private var postAuthor: TextView? = null
    private var postBody: TextView? = null

    private val viewModel : BlogViewModelImpl by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)

        postTitle = findViewById(R.id.title_textview)
        postAuthor = findViewById(R.id.author_textView)
        postBody = findViewById(R.id.body_textView)

        postId = intent.getIntExtra(EXTRA_POST_ID, 0)

        viewModel.fetchPost(postId)
    }

    private fun updateUI(post: Post) {
        postTitle?.text = post.metadata?.title
        postAuthor?.text = post.metadata?.author?.name
        postBody?.text = post.body
    }

    companion object {
        const val TAG = "PostActivity"

        const val EXTRA_POST_ID = "postID"

//        fun newIntent(context: Context, id: Int): Intent {
//            val intent = Intent(context, PostActivity::class.java)
//            intent.putExtra(EXTRA_POST_ID, id)
//            return intent
//        }
    }
}
