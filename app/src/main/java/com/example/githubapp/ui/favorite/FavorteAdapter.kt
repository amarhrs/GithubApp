package com.example.githubapp.ui.favorite

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.githubapp.data.remote.response.FavoriteUserResponse
import com.example.githubapp.databinding.ItemGithubBinding
import com.example.githubapp.ui.detail.DetailActivity


class FavorteAdapter : ListAdapter<FavoriteUserResponse, FavorteAdapter.MyViewHolder>(DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemGithubBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val github = getItem(position)
        holder.bind(github)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailActivity::class.java)
            intentDetail.putExtra(DetailActivity.GITHUB_LOGIN, github.login)
            intentDetail.putExtra(DetailActivity.GITHUB_ID, github.id)
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    class MyViewHolder(private val binding: ItemGithubBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(github: FavoriteUserResponse) {
            binding.tvItem.text = github.login

            Glide.with(itemView.context)
                .load(github.avatarUrl)
                .into(binding.profileImage)
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteUserResponse>() {
            override fun areItemsTheSame(oldItem: FavoriteUserResponse, newItem: FavoriteUserResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: FavoriteUserResponse, newItem: FavoriteUserResponse): Boolean {
                return oldItem == newItem
            }
        }
    }
}