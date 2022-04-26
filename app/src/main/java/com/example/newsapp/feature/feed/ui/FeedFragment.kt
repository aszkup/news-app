package com.example.newsapp.feature.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.databinding.FeedFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class FeedFragment : Fragment() {

    private var _binding: FeedFragmentBinding? = null
    private val binding get() = _binding!!

    private val feedViewModel: FeedViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FeedFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            _binding = this
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = feedViewModel
        setupFeedRecycler()
        setupSwipeToRefresh()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun setupFeedRecycler() {
        val feedPagerAdapter = FeedPagerAdapter(requireActivity(), onItemClickListener = { _, item ->
            navigateToNews(item)
        })
        binding.feedViewPager.adapter = feedPagerAdapter
        binding.feedViewPager.registerOnPageChangeCallback(getOnPageChangedListener())
        feedViewModel.items.observe(viewLifecycleOwner) {
            feedPagerAdapter?.items?.clear()
            feedPagerAdapter?.items?.addAll(it)
            feedPagerAdapter?.notifyDataSetChanged()
            if (feedViewModel.lastPosition <= feedViewModel.items.value?.size ?: 0) {
                binding.feedViewPager.setCurrentItem(feedViewModel.lastPosition, false)
            }
        }
    }

    private fun setupSwipeToRefresh() {
        binding.swipeLayout.setOnRefreshListener { feedViewModel.refresh() }
        feedViewModel.isRefreshing.observe(viewLifecycleOwner) {
            binding.swipeLayout.isRefreshing = it
        }
    }

    private fun getOnPageChangedListener() = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            feedViewModel.lastPosition = position
            feedViewModel.items.value ?: return
            if (position >= feedViewModel.items.value!!.size - 4) {
                feedViewModel.loadNextPage()
            }
        }
    }

    private fun navigateToNews(item: FeedItem) {
        val direction = FeedFragmentDirections.actionFeedFragmentToNewsFragment(news = item)
        findNavController().navigate(direction)
    }
}
