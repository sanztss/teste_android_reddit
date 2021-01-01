package com.fastnews.ui.timeline

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.fastnews.R
import com.fastnews.mechanism.VerifyNetworkInfo
import com.fastnews.service.model.PostData
import com.fastnews.ui.detail.DetailFragment.Companion.KEY_POST
import com.fastnews.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.fragment_timeline.*
import org.koin.android.viewmodel.ext.android.viewModel


class TimelineFragment : Fragment() {

    private val viewModel by viewModel<PostViewModel>()

    private lateinit var adapter: TimelineAdapter

    // Indicate the fullname of an item in the listing to use as the anchor point of the slice.
    // (https://www.reddit.com/dev/api/)
    private var afterKey = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        buildActionBar()
        buildTimeline()
        verifyConnectionState()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.setRvItems(adapter.items)
    }

    private fun verifyConnectionState() {
        context.let {
            if (VerifyNetworkInfo.isConnected(it!!)) {
                hideNoConnectionState()
                if (viewModel.items.size > 0) {
                    afterKey = (viewModel.items[viewModel.items.size - 1]).name
                    adapter.setData(viewModel.items)
                    hideProgress()
                    showPosts()
                    timeline_srl.isRefreshing = false
                } else {
                    showProgress()
                    fetchTimeline()
                }
            } else {
                hideProgress()
                fetchTimelineFromCache()
            }
        }
    }

    private fun buildActionBar() {
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(false) // disable the button
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false) // remove the left caret
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowHomeEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.title = resources.getString(R.string.app_name)
    }

    private fun buildTimeline() {
        adapter = TimelineAdapter { it, imageView ->
            onClickItem(it, imageView)
        }

        val linearLayoutManager = LinearLayoutManager(context)
        timeline_rv.layoutManager = linearLayoutManager
        timeline_rv.itemAnimator = DefaultItemAnimator()
        timeline_rv.adapter = adapter

        timeline_srl.setOnRefreshListener { getLastestPostsListener() }
        timeline_rv.addOnScrollListener(object :
            TimelineLoadMoreListener(linearLayoutManager) {

            override fun isRefreshing(): Boolean {
                return timeline_srl.isRefreshing
            }

            override fun fetchMoreData() {
                timeline_srl.isRefreshing = true
                timeline_rv.post {
                    context.let {
                        if (VerifyNetworkInfo.isConnected(it!!)) {
                            fetchTimelineMoreData()
                        } else {
                            timeline_srl.isRefreshing = false
                        }
                    }
                }
            }
        })
    }

    private fun fetchTimeline() {
        viewModel.getPosts("").observe(viewLifecycleOwner, { posts ->
            posts.let {
                if (posts.size > 0) {
                    viewModel.savePostsOnCache(posts)
                    afterKey = (posts[posts.size - 1]).name
                    adapter.setData(posts)
                    hideProgress()
                    showPosts()
                    timeline_srl.isRefreshing = false
                }
            }
        })
    }

    private fun fetchTimelineMoreData() {
        viewModel.getPosts(afterKey).observe(viewLifecycleOwner, { posts ->
                posts.let {
                    if (posts.size > 0) {
                        viewModel.savePostsOnCache(posts)
                        afterKey = (posts[posts.size - 1]).name
                        adapter.addData(posts)
                        timeline_srl.isRefreshing = false
                    }
                }
            })
    }

    private fun fetchTimelineFromCache() {
        viewModel.getPostsFromCache().observe(viewLifecycleOwner, { posts ->
            posts.let {
                if (posts.size > 0) {
                    afterKey = (posts[posts.size - 1]).name
                    adapter.setData(posts)
                    hideProgress()
                    showPosts()
                    timeline_srl.isRefreshing = false
                }
            }
        })
    }

    private fun showPosts() {
        timeline_rv.visibility = View.VISIBLE
    }

    private fun showProgress() {
        state_progress_timeline.visibility = View.VISIBLE
    }

    private fun hideProgress() {
        state_progress_timeline.visibility = View.GONE
    }

    private fun showNoConnectionState() {
        state_without_conn_timeline.visibility = View.VISIBLE
    }

    private fun hideNoConnectionState() {
        state_without_conn_timeline.visibility = View.GONE
    }

    private fun onClickItem(postData: PostData, imageView: ImageView) {
        val extras = FragmentNavigatorExtras(
            imageView to "thumbnail"
        )
        var bundle = Bundle()
        bundle.putParcelable(KEY_POST, postData)
        findNavController().navigate(R.id.action_timeline_to_detail, bundle, null, extras)
    }

    private fun getLastestPostsListener() {
        context.let {
            if (VerifyNetworkInfo.isConnected(it!!)) {
                fetchTimeline()
                hideNoConnectionState()
            } else {
                timeline_srl.isRefreshing = false
            }
        }
    }
}