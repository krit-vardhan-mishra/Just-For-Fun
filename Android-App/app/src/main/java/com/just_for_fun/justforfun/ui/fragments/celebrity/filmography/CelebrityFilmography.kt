package com.just_for_fun.justforfun.ui.fragments.celebrity.filmography

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.adapters.FilmographyAdapter
import com.just_for_fun.justforfun.databinding.FragmentCelebrityBinding
import com.just_for_fun.justforfun.ui.fragments.celebrity.filmography.CelebrityFilmographyViewModel

class CelebrityFilmography : AppCompatActivity() {
    private lateinit var binding: FragmentCelebrityBinding
    private lateinit var viewModel: CelebrityFilmographyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentCelebrityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val type = intent.getStringExtra("TYPE") ?: "movies"
        viewModel = ViewModelProvider(this)[CelebrityFilmographyViewModel::class.java]
        viewModel.loadFilmography(type)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.activityCelebrityMoviesListView.layoutManager = LinearLayoutManager(this)
        binding.activityCelebrityMoviesListView.adapter = FilmographyAdapter()
    }

    private fun setupObservers() {
        viewModel.filmography.observe(this) { items ->
            (binding.activityCelebrityMoviesListView.adapter as FilmographyAdapter).submitList(items)
        }
    }
}