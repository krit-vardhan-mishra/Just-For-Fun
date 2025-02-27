package com.just_for_fun.justforfun.ui.fragments.search

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapter.PosterAdapter
import com.just_for_fun.justforfun.databinding.FragmentSearchBinding
import com.just_for_fun.justforfun.util.PosterItemDecoration

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupMostSearched()
        setupPreviousSearches()
        setupBasedOnYourSearches()
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<Int>, viewName: String) {
        val adapter = PosterAdapter(items,
            onPosterClick = { imageRes ->
                Toast.makeText(context, "Clicked $viewName poster: $imageRes", Toast.LENGTH_SHORT).show()
            },
            onBookmarkClick = { imageRes ->
                Toast.makeText(context, "Bookmarked $viewName: $imageRes", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
            addItemDecoration(PosterItemDecoration(15))
        }
    }

    private fun setupMostSearched() = setupRecyclerView(
        binding.searchFragmentMostSearchedList,
        viewModel.mostSearchedItems,
        "Most Searched"
    )

    private fun setupPreviousSearches() = setupRecyclerView(
        binding.searchFragmentPreviousSearchList,
        viewModel.previousSearches,
        "Previous Searches"
    )

    private fun setupBasedOnYourSearches() = setupRecyclerView(
        binding.searchFragmentBasedOnYourSearchList,
        viewModel.basedOnYourSearchItems,
        "Based on Your Searches"
    )
}