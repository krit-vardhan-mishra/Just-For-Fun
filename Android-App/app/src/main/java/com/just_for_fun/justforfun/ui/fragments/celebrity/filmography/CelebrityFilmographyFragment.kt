package com.just_for_fun.justforfun.ui.fragments.celebrity.filmography

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.FilmographyAdapter
import com.just_for_fun.justforfun.databinding.FragmentCelebrityFilmographyBinding
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class CelebrityFilmographyFragment : Fragment(R.layout.fragment_celebrity_filmography) {

    private val binding by viewBinding(FragmentCelebrityFilmographyBinding::bind)
    private val viewModel: CelebrityFilmographyViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val type = arguments?.getString("TYPE") ?: "movies"
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