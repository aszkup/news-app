package com.example.newsapp.feature.feed.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.adapter.FragmentViewHolder

class FeedPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val onItemClickListener: (Int, FeedItem) -> Unit
) : FragmentStateAdapter(fragmentActivity) {

    val items = mutableListOf<FeedItem>()

    override fun getItemCount(): Int = items.size

    override fun getItemId(position: Int): Long {
        return items[position].localId
    }

    override fun containsItem(itemId: Long): Boolean {
        return items.firstOrNull { it.localId == itemId } != null
    }

    override fun createFragment(position: Int): Fragment {
        val args = FeedNewsArgs(position, items[position])
        return FeedNewsFragment.newInstance(args)
    }

    override fun onBindViewHolder(holder: FragmentViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        holder.itemView.setOnClickListener { onItemClickListener.invoke(position, items[position]) }
    }
}
