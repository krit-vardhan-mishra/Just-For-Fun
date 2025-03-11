package com.just_for_fun.justforfun.ui.fragments.celebrity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.adapters.AwardsAdapter
import com.just_for_fun.justforfun.adapters.SimilarMoviesAdapter
import com.just_for_fun.justforfun.data.Awards
import com.just_for_fun.justforfun.databinding.FragmentCelebrityBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.ui.fragments.celebrity.awards.CelebrityAwards
import com.just_for_fun.justforfun.ui.fragments.celebrity.filmography.CelebrityFilmography
import com.just_for_fun.justforfun.ui.fragments.movie.MovieFragment
import com.just_for_fun.justforfun.ui.fragments.celebrity.CelebrityViewModel

class CelebrityActivity : AppCompatActivity() {
    private lateinit var binding: FragmentCelebrityBinding
    private lateinit var viewModel: CelebrityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCelebrityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this).get(CelebrityViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setupObservers()
        setupClickListeners()

        viewModel.celebrity.observe(this) { celebrity ->
            // Update UI with celebrity data
        }

        viewModel.movies.observe(this) { movies ->
            // Update UI with movies list
        }

        viewModel.tvShows.observe(this) { tvShows ->
            // Update UI with TV shows list
        }

        viewModel.awards.observe(this) { awards ->
            // Update UI with awards list
        }
    }

    private fun setupObservers() {
        viewModel.movies.observe(this) { movies ->
            setupMoviesAdapter(movies)
        }

        viewModel.tvShows.observe(this) { tvShows ->
            setupTVShowsAdapter(tvShows)
        }

        viewModel.awards.observe(this) { awards ->
            setupAwardsAdapter(awards)
        }
    }

    private fun setupMoviesAdapter(movies: List<MovieItem>) {
        val adapter = SimilarMoviesAdapter(movies) { movie ->
            startActivity(Intent(this, MovieFragment::class.java).apply {
                putExtra("MOVIE_ID", movie.posterUrl)
            })
        }
        binding.activityCelebrityMoviesListView.adapter = adapter
        binding.activityCelebrityMoviesListView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupTVShowsAdapter(tvShows: List<MovieItem>) {
        // Similar to movies adapter
    }

    private fun
            setupAwardsAdapter(awards: List<Awards>) {
        val adapter = AwardsAdapter(awards)
        binding.activityCelebrityAwardsListView.adapter = adapter
        binding.activityCelebrityAwardsListView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun setupClickListeners() {
        binding.activityCelebrityMoviesViewAll.setOnClickListener {
            startActivity(Intent(this, CelebrityFilmography::class.java).apply {
                putExtra("TYPE", "movies")
            })
        }

        binding.activityCelebrityTvShowsViewAll.setOnClickListener {
            startActivity(Intent(this, CelebrityFilmography::class.java).apply {
                putExtra("TYPE", "tvshows")
            })
        }

        binding.activityCelebrityAwardsViewAll.setOnClickListener {
            startActivity(Intent(this, CelebrityAwards::class.java))
        }
    }
}