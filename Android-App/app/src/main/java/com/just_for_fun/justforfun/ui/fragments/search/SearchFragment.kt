package com.just_for_fun.justforfun.ui.fragments.search

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
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

        // Set up filter type selection.
        binding.fragmentSearchSearchType.setOnClickListener {
            showPopupMenu()
        }

        setupBottomNavListener()
        observeViewModel()
        setupViewAll()
        setupSearchViewListener()

        // Add a ViewTreeObserver to log the width after layout
        binding.searchResultsRecyclerView.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = binding.searchResultsRecyclerView.width
                Log.d("SearchFragment", "searchResultsRecyclerView width after layout: $width px")
                // Remove the listener to avoid repeated logs
                binding.searchResultsRecyclerView.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.fragmentSearchSearchType)
        popupMenu.menuInflater.inflate(R.menu.search_nav_bar, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            val selectedType = when (menuItem.itemId) {
                R.id.nav_searchMovie -> "MOVIE"
                R.id.nav_searchTVShow -> "TV SHOW"
                else -> "ALL"
            }
            binding.fragmentSearchSearchType.text = selectedType
            performSearch(binding.searchBar.query.toString())
            true
        }
        popupMenu.show()
    }

    private fun setupSearchViewListener() {
        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                performSearch(query)
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                performSearch(newText)
                return true
            }
        })
    }

    private fun performSearch(query: String) {
        val currentFilter = binding.fragmentSearchSearchType.text.toString()
        viewModel.filterResults(query, currentFilter)
    }

    private fun hideKeyboard() {
        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchBar.windowToken, 0)
    }

    private fun observeViewModel() {
        // Observing default suggestion lists.
        viewModel.mostSearchedItems.observe(viewLifecycleOwner, Observer { items ->
            setupRecyclerView(binding.searchFragmentMostSearchedList, items)
        })
        viewModel.previousSearches.observe(viewLifecycleOwner, Observer { items ->
            setupRecyclerView(binding.searchFragmentPreviousSearchList, items)
        })
        viewModel.basedOnYourSearchItems.observe(viewLifecycleOwner, Observer { items ->
            setupRecyclerView(binding.searchFragmentBasedOnYourSearchList, items)
        })

        // Observe search results and update UI accordingly.
        viewModel.searchResults.observe(viewLifecycleOwner) { filteredItems ->
            val width = binding.searchResultsRecyclerView.width
            Log.d(
                "SearchFragment",
                "searchResultsRecyclerView width before update: $width px, item count: ${filteredItems.size}"
            )

            if (binding.searchBar.query.toString().isNotEmpty()) {
                binding.searchResultsRecyclerView.adapter = null
                binding.searchResultsRecyclerView.layoutManager = LinearLayoutManager(context)
                // Hide suggestion lists
                binding.searchFragmentMostSearchedList.visibility = GONE
                binding.searchFragmentPreviousSearchList.visibility = GONE
                binding.searchFragmentBasedOnYourSearchList.visibility = GONE
                binding.viewAllPreviousSearch.visibility = GONE
                binding.viewAllBasedOnYourSearch.visibility = GONE
                binding.viewAllMostSearched.visibility = GONE
                binding.searchResultsRecyclerView.visibility = VISIBLE
                // Update and show search results RecyclerView
                setupRecyclerView(binding.searchResultsRecyclerView, filteredItems)
            } else {
                // No active search: show suggestions and hide search results.
                binding.searchResultsRecyclerView.visibility = GONE
                binding.searchFragmentMostSearchedList.visibility = VISIBLE
                binding.searchFragmentPreviousSearchList.visibility = VISIBLE
                binding.searchFragmentBasedOnYourSearchList.visibility = VISIBLE
                binding.viewAllPreviousSearch.visibility = VISIBLE
                binding.viewAllBasedOnYourSearch.visibility = VISIBLE
                binding.viewAllMostSearched.visibility = VISIBLE
            }

            val newWidth = binding.searchResultsRecyclerView.width
            Log.d("SearchFragment", "searchResultsRecyclerView width after update: $newWidth px")
        }
    }

    private fun setupRecyclerView(recyclerView: RecyclerView, items: List<MovieItem>) {
        val adapter = PosterAdapter(
            onPosterClick = { position -> openMovieActivity(items[position]) },
            onBookmarkClick = { position ->
                Toast.makeText(context, "Bookmarked ${items[position].title}", Toast.LENGTH_SHORT)
                    .show()
            }
        )
        recyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        recyclerView.apply {
            this.adapter = adapter
            if (recyclerView.itemDecorationCount > 0) {
                recyclerView.removeItemDecorationAt(0)
            }

            recyclerView.addItemDecoration(PosterItemDecoration(15))
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

    private fun setupBottomNavListener() {
        requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            ?.setOnItemReselectedListener { item ->
                if (item.itemId == R.id.nav_searchFragment) {
                    showSearchView()
                }
            }

        val bottomNav =
            requireActivity().findViewById<BottomNavigationView>(R.id.bottomNavigationView)
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

        val imm =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(binding.searchBar, InputMethodManager.SHOW_IMPLICIT)
    }
}