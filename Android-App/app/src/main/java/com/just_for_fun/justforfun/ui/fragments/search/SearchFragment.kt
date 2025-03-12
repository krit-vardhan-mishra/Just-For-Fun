package com.just_for_fun.justforfun.ui.fragments.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
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
    private lateinit var viewModel: SearchViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
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
        val adapter = PosterAdapter(
            posterItems = items.map { it.posterUrl },
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
        val args = Bundle().apply {
            putString("MOVIE_TITLE", movie.title)
            putInt("MOVIE_POSTER", movie.posterUrl)
            putString("MOVIE_DESCRIPTION", movie.description)
            putFloat("MOVIE_RATING", movie.rating)
            putString("MOVIE_TYPE", movie.type)
        }
        findNavController().navigate(R.id.nav_movieFragment, args)
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
