package com.just_for_fun.justforfun.ui.fragments.search

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.PosterAdapter
import com.just_for_fun.justforfun.databinding.FragmentSearchBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.decoration.PosterItemDecoration
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment(R.layout.fragment_search) {

    private val binding by viewBinding(FragmentSearchBinding::bind)
    private val viewModel: SearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupBottomNavListener()
        observeViewModel()
        setupViewAll()
    }

    private fun setupViewAll() {
        binding.viewAllMostSearched.setOnClickListener {
            val bundle = bundleOf(
                "title" to "Movies",
                "subtitle" to "All Movies",
                "dataType" to "movies"
            )
            findNavController().navigate(R.id.nav_postersFragment, bundle)
        }

        binding.viewAllPreviousSearch.setOnClickListener {
            val bundle = bundleOf(
                "title" to "Movies",
                "subtitle" to "All Movies",
                "dataType" to "movies"
            )
            findNavController().navigate(R.id.nav_postersFragment, bundle)
        }

        binding.viewAllBasedOnYourSearch.setOnClickListener {
            val bundle = bundleOf(
                "title" to "Movies",
                "subtitle" to "All Movies",
                "dataType" to "movies"
            )
            findNavController().navigate(R.id.nav_postersFragment, bundle)
        }


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
            onPosterClick = { position -> openMovieActivity(items[position]) },
            onBookmarkClick = { position ->
                Toast.makeText(context, "Bookmarked ${items[position].title}", Toast.LENGTH_SHORT).show()
            }
        )

        recyclerView.apply {
            this.adapter = adapter
            addItemDecoration(PosterItemDecoration(15))
        }

        adapter.submitList(items.map { it.posterUrl })
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
