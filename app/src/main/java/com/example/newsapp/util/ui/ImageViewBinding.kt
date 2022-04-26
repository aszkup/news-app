package com.example.newsapp.util.ui

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

@BindingAdapter("src")
fun setImageUrl(view: ImageView, url: String?) {
    setImageUrl(view, url, null)
}

@BindingAdapter(value = ["src", "placeholder"])
fun setImageUrl(view: ImageView, url: String?, placeholder: Drawable?) {
    view.load(url)
}
