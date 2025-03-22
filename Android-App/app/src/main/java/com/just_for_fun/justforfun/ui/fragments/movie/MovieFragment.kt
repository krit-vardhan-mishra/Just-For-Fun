package com.just_for_fun.justforfun.ui.fragments.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.net.toUri
import com.just_for_fun.justforfun.databinding.FragmentMovieOrShowsSelectedBinding
import com.just_for_fun.justforfun.ui.fragments.movie.movie_or_show.MovieOrShow
import com.just_for_fun.justforfun.ui.fragments.movie.review.Reviews

class MovieFragment : Fragment(R.layout.fragment_movie_or_shows_selected) {

    private val binding by viewBinding(FragmentMovieOrShowsSelectedBinding::bind)
    private val viewModel: MovieViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.getString("MOVIE_TYPE")?.let { type ->
            viewModel.typeText.value = if (type == "Movie") "Movie" else "TV Show"
        }

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        if (savedInstanceState == null) {
            loadMovieOrShowFragment()
        }

        setupNavigation()
        setupTelegramButton()
    }

    private fun setupNavigation() {
        binding.movieOrShow.setOnClickListener {
            loadMovieOrShowFragment()
        }

        binding.review.setOnClickListener {
            loadReviewFragment()
        }
    }

    private fun loadMovieOrShowFragment() {
        val movieOrShowFragment = MovieOrShow()
        movieOrShowFragment.arguments = arguments
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, movieOrShowFragment)
            .commit()
    }

    private fun loadReviewFragment() {
        val reviewFragment = Reviews()
        reviewFragment.arguments = arguments
        childFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, reviewFragment)
            .commit()
    }

    private fun setupTelegramButton() {
        binding.floatingActionButton.setOnClickListener {
            openTelegramLink("https://t.me/JustForFunGroup_04")
        }
    }

    private fun openTelegramLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}
