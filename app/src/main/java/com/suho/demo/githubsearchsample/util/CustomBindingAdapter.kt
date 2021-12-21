package com.suho.demo.githubsearchsample.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suho.demo.githubsearchsample.data.model.GithubRepo
import com.suho.demo.githubsearchsample.view.MainAdapter

object CustomBindingAdapter {

    @JvmStatic
    @BindingAdapter("repos")
    fun bindRepos(recyclerView: RecyclerView, items: List<GithubRepo>?) {
        val adapter = recyclerView.adapter as MainAdapter
        adapter.submitList(items?.toList())
    }
}