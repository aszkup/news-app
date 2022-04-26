package com.example.newsapp.feature.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.newsapp.databinding.FeedNewsFragmentBinding
import java.io.Serializable

class FeedNewsFragment : Fragment() {

    private var _binding: FeedNewsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
        FeedNewsFragmentBinding.inflate(inflater, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
            _binding = this
        }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = (requireArguments().getSerializable(ARGS) as FeedNewsArgs)
        binding.uiModel = args.item
        binding.executePendingBindings()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val ARGS = "FeedNewsArgs"

        fun newInstance(args: FeedNewsArgs) = FeedNewsFragment().apply {
            arguments = bundleOf(ARGS to args)
        }
    }
}

data class FeedNewsArgs(
    val position: Int,
    val item: FeedItem
) : Serializable
