package com.just_for_fun.justforfun.ui.fragments.movie.movie_or_show

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.CastCrewAdapter
import com.just_for_fun.justforfun.adapters.SimilarMoviesAdapter
import com.just_for_fun.justforfun.databinding.FragmentMovieOrShowBinding
import com.just_for_fun.justforfun.ui.fragments.poster.PosterFragment
import com.just_for_fun.justforfun.util.PosterItemDecoration
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MovieOrShow : Fragment(R.layout.fragment_movie_or_show) {

    private val binding by viewBinding(FragmentMovieOrShowBinding::bind)
    private val viewModel: MovieOrShowViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Set movie details from arguments
        arguments?.let {
            viewModel.setMovieDetails(
                title = it.getString("MOVIE_TITLE") ?: "Unknown",
                posterUrl = it.getInt("MOVIE_POSTER"),
                description = it.getString("MOVIE_DESCRIPTION") ?: "",
                rating = it.getFloat("MOVIE_RATING"),
                type = it.getString("MOVIE_TYPE") ?: "Movie"
            )
        }

        // Initialize adapters and observers
        setupAdapters()
        observeViewModel()
        viewModel.loadCastAndCrew()
        setupViewAll()
    }

    private fun setupViewAll() {
        binding.btnViewAllSimilarMovies.setOnClickListener {
            val movieType = requireArguments().getString("MOVIE_TYPE") ?: "Movie"
            val dataType = if (movieType == "Movie") "movies" else "tvShows"

            val bundle = bundleOf(
                "title" to (requireArguments().getString("MOVIE_TITLE") ?: "Unknown"),
                "subtitle" to "All",
                "dataType" to dataType
            )
            findNavController().navigate(R.id.nav_postersFragment, bundle)
        }

        binding.btnViewAllCastAndCrew.setOnClickListener {
            val bundle = bundleOf(
                "title" to "Cast And Crew",
                "subtitle" to "All",
                "dataType" to "cast_and_crew"
            )
            findNavController().navigate(R.id.nav_postersFragment, bundle)
        }
    }

    private fun setupAdapters() {
        // Initialize CastCrewAdapter with an empty list
        val castAdapter = CastCrewAdapter(emptyList()) { member ->
            viewModel.selectCastMember(member)
        }

        // Initialize SimilarMoviesAdapter with an empty list
        val similarMoviesAdapter = SimilarMoviesAdapter(emptyList()) { movie ->
            viewModel.setMovieDetails(
                title = movie.title,
                posterUrl = movie.posterUrl,
                description = movie.description,
                rating = movie.rating,
                type = movie.type
            )
        }

        // Set up RecyclerView for Cast & Crew
        binding.movieActivityCastAndCrew.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            addItemDecoration(PosterItemDecoration(15))
        }

        // Set up RecyclerView for Similar Movies
        binding.movieActivityMoreLikeThis.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = similarMoviesAdapter
            addItemDecoration(PosterItemDecoration(15))
        }
    }

    private fun observeViewModel() {
        // Observe movie details and update UI
        viewModel.movieDetails.observe(viewLifecycleOwner) { details ->
            binding.activityMovieTitle.text = details.title
            binding.activityMovieMovieOrShow.text = details.type
            binding.activityMovieDescription.text = details.description
            binding.activityMovieRatingBar.rating = details.rating
            binding.activityMovieUserRating.text = "${details.rating}/5"

            // Load movie poster using Glide
            Glide.with(requireContext())
                .load(details.posterUrl)
                .placeholder(R.drawable.placeholder_image)
                .into(binding.activityMoviePoster)
        }

        // Observe cast and crew data and update adapter
        viewModel.castAndCrew.observe(viewLifecycleOwner) { cast ->
            (binding.movieActivityCastAndCrew.adapter as? CastCrewAdapter)?.submitList(cast)
        }

        // Observe similar movies data and update adapter
        viewModel.similarMovies.observe(viewLifecycleOwner) { movies ->
            Log.d("MovieOrShow", "Similar movies list size: ${movies.size}")
            movies.forEach { movie ->
                Log.d("MovieOrShow", "Similar Movie Title: ${movie.title}, Poster URL: ${movie.posterUrl}")
            }

            (binding.movieActivityMoreLikeThis.adapter as? SimilarMoviesAdapter)?.submitList(movies)
        }

        // Observe selected cast member and navigate to celebrity fragment
        viewModel.selectedCastMember.observe(viewLifecycleOwner) { castMember ->
            castMember?.let {
                // Pass the cast member's ID to the celebrity fragment
                val bundle = Bundle().apply {
                    putString("CELEBRITY_ID", it.id)
                }
                findNavController().navigate(R.id.nav_celebrityFragment, bundle)
                viewModel.onCastMemberNavigated()
            }
        }

        // Observe test cases and load more similar movies if needed
        viewModel.testCases.observe(viewLifecycleOwner) { testCases ->
            if (testCases != null) {
                val type = arguments?.getString("MOVIE_TYPE") ?: "Movie"
                viewModel.setupMoreLikeThis(type)
            }
        }
    }
}