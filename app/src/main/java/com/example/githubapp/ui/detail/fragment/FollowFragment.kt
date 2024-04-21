package com.example.githubapp.ui.detail.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapp.data.remote.response.FavoriteUserResponse
import com.example.githubapp.databinding.FragmentFollowBinding
import com.example.githubapp.ui.detail.fragment.adapter.FollowAdapter

class FollowFragment : Fragment() {

    private lateinit var binding: FragmentFollowBinding
    private val followViewModel: FollowViewModel by viewModels()

    companion object {
        const val ARG_POSITION = "section_number"
        const val ARG_USERNAME = "username"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowBinding.inflate(inflater, container, false)
        return (binding.root)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireActivity())
        binding.rvListFollow.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(requireActivity(), layoutManager.orientation)
        binding.rvListFollow.addItemDecoration(itemDecoration)

        followViewModel.follow.observe(viewLifecycleOwner) { follow ->
            follow?.let { setGithubData(it) }
        }

        val position = arguments?.getInt(ARG_POSITION, 0)
        val username = arguments?.getString(ARG_USERNAME, "")

        if (position == 1) {
            followViewModel.getListFollowers(username.toString())
        } else {
            followViewModel.getListFollowing(username.toString())
        }

        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
    }

    private fun setGithubData(item: List<FavoriteUserResponse>) {
        val adapter = FollowAdapter()
        adapter.submitList(item)
        binding.rvListFollow.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}