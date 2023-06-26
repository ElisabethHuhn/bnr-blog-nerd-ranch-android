package com.bignerdranch.android.blognerdranch.controller.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.slidingpanelayout.widget.SlidingPaneLayout
import com.bignerdranch.android.blognerdranch.databinding.FragmentFirstBinding
import com.bignerdranch.android.blognerdranch.model.PostMetadata
import com.bignerdranch.android.blognerdranch.viewmodel.BlogViewModelImpl

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!
    private val viewModel : BlogViewModelImpl by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // TODO: move to activity and only call once
        viewModel.fetchPosts() // Make the API fetch

        val slidingPaneLayout = binding.slidingPaneLayout
        slidingPaneLayout.lockMode = SlidingPaneLayout.LOCK_MODE_LOCKED
        // Connect the SlidingPaneLayout to the system back button.
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            PostListOnBackPressedCallback(slidingPaneLayout)
        )

        val recyclerView: RecyclerView = binding.postList

        /*
         * Define the Recyclerview event listeners
         */
        /** Click Listener to trigger retrofit call to get the post indicated
         */
        val onItemClicked = { postMetadata: PostMetadata? ->
            //initiate retrofit call to fetch the individual post indicated
            viewModel.fetchPost(postMetadata?.postId ?: 0)
            //the callback (triggered when a response post is received) is defined within the ViewModel
            //This callback will update the currentPost, which in turn will trigger the detail pane
        }

        //set up observer of the list of post meta data
        viewModel.posts.observe(viewLifecycleOwner) {
            setupRecyclerView(
                recyclerView = recyclerView,
                onItemClicked = onItemClicked
            )
        }

        //set up observer of the single post
        viewModel.currentPost.observe(viewLifecycleOwner) {
            //trigger navigation based on if you have a single pane layout or two pane layout
            // Slide the detail pane into view.
            // If both panes are visible, this has no visible effect.
            binding.slidingPaneLayout.openPane()
        }
    }

    private fun setupRecyclerView(
        recyclerView: RecyclerView,
        onItemClicked: (PostMetadata?) -> Unit,
    ) {
        // Initialize the adapter and set it to the RecyclerView.
        val adapter = PostAdapter (
            onItemClicked = onItemClicked,
            postMetadata = viewModel.posts.value
        )
        recyclerView.adapter = adapter
        val values = viewModel.posts.value
        adapter.submitList(values)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

/**
 * Callback providing custom back navigation.
 */

class PostListOnBackPressedCallback(
    private val slidingPaneLayout: SlidingPaneLayout
) : OnBackPressedCallback(
    // Set the default 'enabled' state to true only if it is slidable (i.e., the panes
    // are overlapping) and open (i.e., the detail pane is visible).
    slidingPaneLayout.isSlideable && slidingPaneLayout.isOpen
), SlidingPaneLayout.PanelSlideListener {

    init {
        slidingPaneLayout.addPanelSlideListener(this)
    }

    override fun handleOnBackPressed() {
        // Return to the list pane when the system back button is pressed.
        slidingPaneLayout.closePane()
    }

    override fun onPanelSlide(panel: View, slideOffset: Float) {}

    override fun onPanelOpened(panel: View) {
        // Intercept the system back button when the detail pane becomes visible.
        isEnabled = true
    }

    override fun onPanelClosed(panel: View) {
        // Disable intercepting the system back button when the user returns to the
        // list pane.
        isEnabled = false
    }
}
