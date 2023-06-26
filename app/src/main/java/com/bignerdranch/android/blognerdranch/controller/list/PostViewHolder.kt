package com.bignerdranch.android.blognerdranch.controller.list

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.bignerdranch.android.blognerdranch.R
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.controller.post.PostActivity
import com.bignerdranch.android.blognerdranch.databinding.ItemListContentBinding

class PostViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    var postMetadata: PostMetadata? = null

    val titleTextView: TextView = itemView.findViewById(android.R.id.text1)

//    init {
//        itemView.setOnClickListener(this)
//    }

    fun bind(postMetadata: PostMetadata) {
        this.postMetadata = postMetadata
        titleTextView.text = postMetadata.title
    }

//    override fun onClick(v: View) {
//        val context = v.context
//        context.startActivity(PostActivity.newIntent(v.context, postMetadata!!.postId!!))
//    }

}