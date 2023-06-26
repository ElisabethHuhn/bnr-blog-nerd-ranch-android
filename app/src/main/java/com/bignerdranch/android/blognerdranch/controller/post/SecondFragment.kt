package com.bignerdranch.android.blognerdranch.controller.post

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bignerdranch.android.blognerdranch.databinding.FragmentSecondBinding
import com.bignerdranch.android.blognerdranch.model.Post
import com.bignerdranch.android.blognerdranch.viewmodel.BlogViewModelImpl

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class SecondFragment : Fragment() {

//    private var toolbarLayout: CollapsingToolbarLayout? = null

//    private lateinit var buildTypeTextView: TextView

    private lateinit var postTitle: TextView
    private lateinit var postAuthor: TextView
    private lateinit var postBody: TextView

    private var _binding: FragmentSecondBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private val viewModel : BlogViewModelImpl by viewModels()

    private fun updateUI(post: Post) {
        postTitle.text = post.metadata?.title
        postAuthor.text = post.metadata?.author?.name
        postBody.text = post.body
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //go fetch data if necessary
        if (viewModel.currentPost.value == null)
            viewModel.fetchPost(viewModel.currentPostId.value ?: 0)

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        postTitle = binding.titleTextview
        postAuthor = binding.authorTextView
        postBody = binding.bodyTextView

        viewModel.currentPost.value?.let {
            updateUI(it)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Attach an observer on the currentCharacter to update the UI automatically
        // when the selection changes
        viewModel.currentPost.observe(this.viewLifecycleOwner) {
            viewModel.currentPost.value?.let {
                updateUI(it)
            }

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}