package com.example.githubapp.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.githubapp.R
import com.example.githubapp.data.local.entity.FavoriteUser
import com.example.githubapp.databinding.ActivityDetailBinding
import com.example.githubapp.ui.detail.fragment.adapter.SectionsPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import java.lang.StringBuilder

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private var favoriteUser = FavoriteUser()
    var favorite = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val detailViewModel: DetailViewModel by viewModels { DetailViewModelFactory(application) }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        var getIntent: String? = null
        val intentDetail = intent

        if (intentDetail != null && intentDetail.hasExtra(GITHUB_LOGIN)) {
            getIntent = intentDetail.getStringExtra(GITHUB_LOGIN).toString()
        }

        sectionsPagerAdapter.setUsername(getIntent!!)

        detailViewModel.detailUser(getIntent.toString())

        detailViewModel.detailUser.observe(this) {
            if (it != null) {
                binding.tvName.text = it.name
                binding.tvUsername.text = it.login
                Glide.with(this)
                    .load(it.avatarUrl)
                    .into(binding.profileImage)
                binding.tvFollowers.text =
                    StringBuilder(it.followers.toString()).append(" Followers")
                binding.tvFollowing.text =
                    StringBuilder(it.following.toString()).append(" Following")

                favoriteUser.id = intentDetail.getIntExtra(GITHUB_ID, 0)
                favoriteUser.username = it.login
                favoriteUser.avatarUrl = it.avatarUrl
            }
        }

        binding.fabAdd.setOnClickListener {
            if (favorite) {
                detailViewModel.deleteToDb(favoriteUser)
            } else {
                detailViewModel.insertToDb(favoriteUser)
            }
        }

        detailViewModel.favoriteByUsername(getIntent).observe(this) {
            if (it != null) {
                binding.fabAdd.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabAdd.context,
                        R.drawable.baseline_favorite_24
                    )
                )
                favorite = true
            } else {
                binding.fabAdd.setImageDrawable(
                    ContextCompat.getDrawable(
                        binding.fabAdd.context,
                        R.drawable.baseline_favorite_border_24
                    )
                )
                favorite = false
            }
        }

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showError(errorMessage)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showError(errorMessage: String) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        const val GITHUB_LOGIN = "arif"
        const val GITHUB_ID = "id"
    }

}