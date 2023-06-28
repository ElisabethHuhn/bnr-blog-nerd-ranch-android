package com.bignerdranch.android.blognerdranch.controller.list

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bignerdranch.android.blognerdranch.model.PostMetadata

class PostViewHolder(
    itemView: View
) : RecyclerView.ViewHolder(itemView) {

    private var postMetadata: PostMetadata? = null

    private val titleTextView: TextView = itemView.findViewById(android.R.id.text1)

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