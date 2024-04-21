package com.example.githubapp.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.R
import com.example.githubapp.data.remote.response.ItemItems
import com.example.githubapp.databinding.ActivityMainBinding
import com.example.githubapp.ui.favorite.FavoriteActivity
import com.example.githubapp.ui.main.adapter.GithubAdapter
import com.example.githubapp.ui.setting.SettingActivity
import com.example.githubapp.ui.setting.SettingPreferences
import com.example.githubapp.ui.setting.dataStore


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pref = SettingPreferences.getInstance(application.dataStore)
        val mainViewModel = ViewModelProvider(this, MainViewModelFactory(pref)).get(
            MainViewModel::class.java
        )

        supportActionBar?.hide()

        val layoutManager = LinearLayoutManager(this)
        binding.rvListUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvListUser.addItemDecoration(itemDecoration)


        mainViewModel.listGithub.observe(this) { listGithub ->
            setGithubData(listGithub)
        }
        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    mainViewModel.getGithubUser(searchView.text.toString())
                    searchView.hide()
                    false
                }
            searchBar.inflateMenu(R.menu.option_menu)
            searchBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.favorite -> {
                        val intent = Intent(this@MainActivity, FavoriteActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.setting -> {
                        val intent = Intent(this@MainActivity, SettingActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }
        }

        mainViewModel.errorMessage.observe(this) { errorMessage ->
            if (errorMessage != null) {
                showError(errorMessage)
            }
        }
    }

    private fun setGithubData(item: List<ItemItems>) {
        val adapter = GithubAdapter()
        adapter.submitList(item)
        binding.rvListUser.adapter = adapter
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
}