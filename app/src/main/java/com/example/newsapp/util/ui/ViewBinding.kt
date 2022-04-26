package com.example.newsapp.util.ui

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter

@BindingAdapter("isVisible")
fun setVisibility(view: View, isVisible: Boolean?) {
    isVisible?.let { view.isVisible = it }
}
