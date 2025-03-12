package com.just_for_fun.justforfun.ui.fragments.celebrity.filmography

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.FilmographyAdapter
import com.just_for_fun.justforfun.databinding.FragmentCelebrityBinding
import com.just_for_fun.justforfun.databinding.FragmentCelebrityFilmographyBinding

class CelebrityFilmographyFragment : Fragment(R.layout.fragment_celebrity_filmography) {
    private lateinit var binding: FragmentCelebrityFilmographyBinding
    private lateinit var viewModel: CelebrityFilmographyViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCelebrityFilmographyBinding.bind(view)

        val type = arguments?.getString("TYPE") ?: "movies"
        viewModel = ViewModelProvider(this)[CelebrityFilmographyViewModel::class.java]
        viewModel.loadFilmography(type)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        binding.celebrityFilmography.layoutManager = LinearLayoutManager(requireContext())
        binding.celebrityFilmography.adapter = FilmographyAdapter()
    }

    private fun setupObservers() {
        viewModel.filmography.observe(viewLifecycleOwner) { items ->
            (binding.celebrityFilmography.adapter as FilmographyAdapter).submitList(items)
        }
    }
}