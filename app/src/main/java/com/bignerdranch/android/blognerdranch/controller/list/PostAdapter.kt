package com.bignerdranch.android.blognerdranch.controller.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.bignerdranch.android.blognerdranch.model.PostMetadata

class PostAdapter(
    private val onItemClicked: (PostMetadata?) -> Unit,
    var postMetadata: List<PostMetadata>?
) : ListAdapter<PostMetadata, PostViewHolder>(DiffCallback) {

    override fun getItemCount(): Int {
        return postMetadata?.size ?: 0
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
): PostViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)

        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentPost = postMetadata?.get(position)
        holder.itemView.setOnClickListener { onItemClicked(currentPost) }
        currentPost?.let {
            holder.bind(it)
        }

    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<PostMetadata>() {
            override fun areItemsTheSame(oldItem: PostMetadata, newItem: PostMetadata): Boolean {
                val isIdMatch = when {
                    (oldItem.postId == null) && (newItem.postId == null) -> true
                    (oldItem.postId != null) && (newItem.postId == null) -> false
                    (oldItem.postId == null)  -> false
                    else -> (oldItem.postId == newItem.postId)
                }
                if(!isIdMatch) return false

                val isTitleMatch = when {
                    (oldItem.title == null) && (newItem.title == null) -> true
                    (oldItem.title != null) && (newItem.title == null) -> false
                    (oldItem.title == null)  -> false
                    else -> (oldItem.title == newItem.title)
                }
                if(!isTitleMatch) return false

                val isSummaryMatch = when {
                    (oldItem.summary == null) && (newItem.summary == null) -> true
                    (oldItem.summary != null) && (newItem.summary == null) -> false
                    (oldItem.summary == null)  -> false
                    else -> (oldItem.summary == newItem.summary)
                }
                if(!isSummaryMatch) return false

                val isAuthorMatch = when {
                    (oldItem.author == null) && (newItem.author == null) -> true
                    (oldItem.author != null) && (newItem.author == null) -> false
                    (oldItem.author == null) -> false
                    else -> (oldItem.author == newItem.author)
                }
                if(!isAuthorMatch) return false

                val isPublishMatch = when {
                    (oldItem.publishDate == null) && (newItem.publishDate == null) -> true
                    (oldItem.publishDate != null) && (newItem.publishDate == null) -> false
                    (oldItem.publishDate == null)  -> false
                    else -> (oldItem.publishDate == newItem.publishDate)
                }
                return isPublishMatch
            }

            override fun areContentsTheSame(oldItem: PostMetadata, newItem: PostMetadata): Boolean {
                return oldItem == newItem
            }
        }
    }
}