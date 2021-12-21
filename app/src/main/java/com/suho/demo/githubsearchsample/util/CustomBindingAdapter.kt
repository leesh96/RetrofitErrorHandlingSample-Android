package com.suho.demo.githubsearchsample.util

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suho.demo.githubsearchsample.data.model.GithubRepo
import com.suho.demo.githubsearchsample.view.MainAdapter

object CustomBindingAdapter {

    @JvmStatic
    @BindingConversion
    fun convertBooleanToVisibility(visible: Boolean) = if (visible) View.VISIBLE else View.GONE

    @JvmStatic
    @BindingAdapter("repos")
    fun bindRepos(recyclerView: RecyclerView, items: List<GithubRepo>?) {
        val adapter = recyclerView.adapter as MainAdapter
        adapter.submitList(items?.toList())
    }

    @JvmStatic
    @BindingAdapter("imageUrl")
    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView).load(url).into(imageView)
    }
}