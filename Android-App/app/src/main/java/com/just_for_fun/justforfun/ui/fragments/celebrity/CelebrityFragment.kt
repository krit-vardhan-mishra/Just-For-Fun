package com.just_for_fun.justforfun.ui.fragments.celebrity

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.SimilarMoviesAdapter
import com.just_for_fun.justforfun.databinding.FragmentCelebrityBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CelebrityFragment : Fragment(R.layout.fragment_celebrity) {

    private val binding by viewBinding(FragmentCelebrityBinding::bind)
    private val viewModel: CelebrityViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Get celebrity ID from arguments
        arguments?.getString("CELEBRITY_ID")?.let { celebrityId ->
            viewModel.setCelebrityId(celebrityId)
        }

        setupObservers()
        setupClickListeners()
        observeCelebrityDetails()
    }

    private fun observeCelebrityDetails() {
        viewModel.celebrity.observe(viewLifecycleOwner) { celebrity ->
            celebrity?.let {
                binding.activityCelebrityName.text = it.name
                binding.activityCelebrityBio.text = it.bio
                Glide.with(this)
                    .load(it.imageUrl)
                    .placeholder(R.drawable.placeholder_image)
                    .into(binding.activityCelebrityPhoto)
                binding.activityCelebrityAge.text = "${it.age} Years"
                binding.activityCelebrityFilmographyCount.text = "${it.filmographyCount} Movies"
                binding.activityCelebrityAwardsCounts.text = "${it.awardsCount} Awards"
            }
        }
    }

    private fun setupObservers() {
        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            if (movies.isNotEmpty()) {
                setupMoviesAdapter(movies)
            } else {
                Log.e("CelebrityFragment", "Movies list is empty")
            }
        }

        viewModel.tvShows.observe(viewLifecycleOwner) { tvShows ->
            setupTVShowsAdapter(tvShows)
        }

        viewModel.awards.observe(viewLifecycleOwner) { awards ->
            setupAwardsClickListener()
        }
    }

    private fun setupMoviesAdapter(movies: List<MovieItem>) {
        Log.d("CelebrityFragment", "Movies list size: ${movies.size}")
        movies.forEach { movie ->
            Log.d("CelebrityFragment", "Movie Title: ${movie.title}, Poster URL: ${movie.posterUrl}")
        }

        val adapter = SimilarMoviesAdapter { movie ->
            navigateToMovieFragment(movie, "movie")
        }
        adapter.submitList((movies))
        binding.activityCelebrityMoviesListView.adapter = adapter
        binding.activityCelebrityMoviesListView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupTVShowsAdapter(tvShows: List<MovieItem>) {
        Log.d("CelebrityFragment", "TV Shows list size: ${tvShows.size}")
        tvShows.forEach { show ->
            Log.d("CelebrityFragment", "TV Show Title: ${show.title}, Poster URL: ${show.posterUrl}")
        }

        val adapter = SimilarMoviesAdapter { show ->
            navigateToMovieFragment(show, "tvshow")
        }
        adapter.submitList(tvShows)
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