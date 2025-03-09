package com.just_for_fun.justforfun.ui.fragments.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.PosterAdapter
import com.just_for_fun.justforfun.databinding.FragmentSearchBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.ui.fragments.movie.MovieFragment
import com.just_for_fun.justforfun.util.PosterItemDecoration

class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var binding: FragmentSearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupBottomNavListener()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.mostSearchedItems.observe(viewLifecycleOwner, Observer { items ->
            setupRecyclerView(binding.searchFragmentMostSearchedList, items)
        })

        viewModel.previousSearches.observe(viewLifecycleOwner, Observer { items ->
            setupRecyclerView(binding.searchFragmentPreviousSearchList, items)
        })

        viewModel.basedOnYourSearchItems.observe(viewLifecycleOwner, Observer { items ->
            setupRecyclerView(binding.searchFragmentBasedOnYourSearchList, items)
        })
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<MovieItem>) {
        val adapter = PosterAdapter(items.map { it.posterUrl },
            onPosterClick = { position -> openMovieActivity(items[position]) },
            onBookmarkClick = { position ->
                Toast.makeText(context, "Bookmarked ${items[position].title}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            this.adapter = adapter
            addItemDecoration(PosterItemDecoration(15))
        }
    }

    private fun openMovieActivity(movie: MovieItem) {
        val intent = Intent(requireContext(), MovieFragment::class.java).apply {
            putExtra("MOVIE_TITLE", movie.title)
            putExtra("MOVIE_POSTER", movie.posterUrl)
            putExtra("MOVIE_DESCRIPTION", movie.description)
            putExtra("MOVIE_RATING", movie.rating)
            putExtra("MOVIE_TYPE", movie.type)
        }
        startActivity(intent)
    }

    private fun setupBottomNavListener() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            ?.setOnItemReselectedListener { item ->
                if (item.itemId == R.id.nav_searchFragment) {
                    showSearchView()
                }
            }

        val bottomNav = requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val searchMenuView = bottomNav.findViewById<View>(R.id.nav_searchFragment)

        searchMenuView?.setOnLongClickListener {
            showSearchView()
            true
        }
    }

    private fun showSearchView() {
        if (!isAdded) return

        binding.searchBar.isIconified = false
        binding.searchBar.requestFocus()

        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchBar, InputMethodManager.SHOW_IMPLICIT)
    }
}