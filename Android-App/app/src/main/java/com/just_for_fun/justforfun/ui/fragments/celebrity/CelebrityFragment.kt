package com.just_for_fun.justforfun.ui.fragments.celebrity

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.SimilarMoviesAdapter
import com.just_for_fun.justforfun.databinding.FragmentCelebrityBinding
import com.just_for_fun.justforfun.items.MovieItem

class CelebrityFragment : Fragment(R.layout.fragment_celebrity) {
    private lateinit var binding: FragmentCelebrityBinding
    private lateinit var viewModel: CelebrityViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCelebrityBinding.bind(view)

        viewModel = ViewModelProvider(this)[CelebrityViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupObservers()
        setupClickListeners()
    }

    private fun setupObservers() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            setupMoviesAdapter(movies)
        }

        viewModel.tvShows.observe(viewLifecycleOwner) { tvShows ->
            setupTVShowsAdapter(tvShows)
        }

        viewModel.awards.observe(viewLifecycleOwner) { awards ->
            setupAwardsClickListener()
        }
    }

    private fun setupMoviesAdapter(movies: List<MovieItem>) {
        val adapter = SimilarMoviesAdapter(movies) { movie ->
            navigateToMovieFragment(movie, "movie")
        }
        binding.activityCelebrityMoviesListView.adapter = adapter
        binding.activityCelebrityMoviesListView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupTVShowsAdapter(tvShows: List<MovieItem>) {
        val adapter = SimilarMoviesAdapter(tvShows) { show ->
            navigateToMovieFragment(show, "tvshow")
        }
        binding.activityCelebrityTvShowsListView.adapter = adapter
        binding.activityCelebrityTvShowsListView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupAwardsClickListener() {
        binding.activityCelebrityAwardsViewAll.setOnClickListener {
            findNavController().navigate(R.id.nav_celebrityFragmentAwards)
        }
    }

    private fun setupClickListeners() {
        binding.activityCelebrityMoviesViewAll.setOnClickListener {
            navigateToFilmography("movies")
        }

        binding.activityCelebrityTvShowsViewAll.setOnClickListener {
            navigateToFilmography("tvshows")
        }
    }

    private fun navigateToMovieFragment(movie: MovieItem, type: String) {
        findNavController().navigate(
            R.id.nav_movieFragment,
            Bundle().apply {
                putString("MOVIE_TITLE", movie.title)
                putInt("MOVIE_POSTER", movie.posterUrl)
                putString("MOVIE_DESCRIPTION", movie.description)
                putFloat("MOVIE_RATING", movie.rating)
                putString("MOVIE_TYPE", type)
            }
        )
    }

    private fun navigateToFilmography(type: String) {
        findNavController().navigate(
            R.id.nav_celebrityFragmentFilmography,
            Bundle().apply { putString("TYPE", type) }
        )
    }
}