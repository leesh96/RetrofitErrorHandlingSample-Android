package com.suho.demo.githubsearchsample.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suho.demo.githubsearchsample.data.model.GithubRepo
import com.suho.demo.githubsearchsample.databinding.ItemReposBinding

class MainAdapter : ListAdapter<GithubRepo, MainAdapter.MainViewHolder>(mainDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val itemBinding =
            ItemReposBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MainViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MainViewHolder(private val itemBinding: ItemReposBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(item: GithubRepo) {
            itemBinding.item = item

            Glide.with(itemBinding.root).load(item.ownerAvatar).into(itemBinding.ivAvatar)
        }
    }

    companion object {
        val mainDiffUtil = object : DiffUtil.ItemCallback<GithubRepo>() {
            override fun areItemsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: GithubRepo, newItem: GithubRepo): Boolean {
                return oldItem == newItem
            }
        }
    }
}