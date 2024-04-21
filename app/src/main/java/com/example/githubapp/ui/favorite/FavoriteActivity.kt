package com.example.githubapp.ui.favorite

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.data.remote.response.FavoriteUserResponse
import com.example.githubapp.databinding.ActivityFavoriteBinding

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    val adapter = FavorteAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val favoriteViewModel: FavoriteViewModel by viewModels { FavoriteViewModelFactory(application) }

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavorite.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvFavorite.addItemDecoration(itemDecoration)


        favoriteViewModel.getFavoriteUser().observe(this) { user ->
            val items = arrayListOf<FavoriteUserResponse>()
            user.map {
                val item = FavoriteUserResponse(id = it.id, login = it.username.toString(), avatarUrl = it.avatarUrl.toString())
                items.add(item)
            }
            adapter.submitList(items)
            binding.rvFavorite.adapter = adapter
        }



    }

}