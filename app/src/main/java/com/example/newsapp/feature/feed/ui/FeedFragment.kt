package com.example.newsapp.feature.feed.ui

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.newsapp.R
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
        val pageMarginPx = dpToPx(resources.getDimension(R.dimen.page_margin).toInt())
        val pagePaddingPx = dpToPx(resources.getDimension(R.dimen.page_padding).toInt())
        val marginTransformer = MarginPageTransformer(pageMarginPx)
        binding.feedViewPager.apply {
            adapter = feedPagerAdapter
            registerOnPageChangeCallback(getOnPageChangedListener())
            clipToPadding = false
            clipChildren = false
            offscreenPageLimit = 2
            setPadding(0, pagePaddingPx, 0, pagePaddingPx)
            setPageTransformer(marginTransformer)
        }
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

    private fun dpToPx(dp: Int): Int {
        val displayMetrics = requireContext().resources.displayMetrics
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}

