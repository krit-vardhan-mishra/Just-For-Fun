package com.just_for_fun.justforfun.ui.fragments.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.ImageSliderAdapter
import com.just_for_fun.justforfun.adapters.MoviesAdapter
import com.just_for_fun.justforfun.adapters.TVShowsAdapter
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupImageSlider()
        setupMoviesRecyclerView()
        setupTVShowsRecyclerView()
        setupAutoSlider()
    }

    private fun setupImageSlider() {
        val images = listOf(
            R.drawable.two, R.drawable.three, R.drawable.four,
            R.drawable.six, R.drawable.five, R.drawable.eight,
            R.drawable.nine, R.drawable.ten, R.drawable.seven,
            R.drawable.eleven, R.drawable.twelve, R.drawable.one,
            R.drawable.thirteen, R.drawable.fourteen, R.drawable.fifteen
        )

        val sliderAdapter = ImageSliderAdapter(requireContext(), images)
        binding.viewPager.adapter = sliderAdapter
    }

    private fun setupMoviesRecyclerView() {
        binding.fragmentHomeMoviesRecyclerViewer.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            binding.fragmentHomeMoviesRecyclerViewer.adapter =
                MoviesAdapter(movies ?: emptyList(), ::onMovieClick)
        }
    }

    private fun setupTVShowsRecyclerView() {
        binding.fragmentHomeShowsRecyclerViewer.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        viewModel.tvShows.observe(viewLifecycleOwner) { tvShows ->
            binding.fragmentHomeShowsRecyclerViewer.adapter =
                TVShowsAdapter(tvShows ?: emptyList(), ::onTVShowClick)
        }
    }

    fun onMovieClick(movie: Movies) {
        val bundle = bundleOf(
            "MOVIE_TITLE" to movie.title,
            "MOVIE_POSTER" to movie.posterUrl,
            "MOVIE_DESCRIPTION" to movie.description,
            "MOVIE_RATING" to movie.rating,
            "MOVIE_TYPE" to "Movie"
        )
        findNavController().navigate(R.id.nav_movieFragment, bundle)
    }

    fun onTVShowClick(tvShow: TVShows) {
        val bundle = bundleOf(
            "MOVIE_TITLE" to tvShow.title,
            "MOVIE_POSTER" to tvShow.posterUrl,
            "MOVIE_DESCRIPTION" to tvShow.description,
            "MOVIE_RATING" to tvShow.rating,
            "MOVIE_TYPE" to "TV Show"
        )
        findNavController().navigate(R.id.nav_movieFragment, bundle)
    }

    private fun setupAutoSlider() {
        sliderHandler = Handler(Looper.getMainLooper())
        sliderRunnable = Runnable {
            val currentItem = binding.viewPager.currentItem
            val totalItems = binding.viewPager.adapter?.itemCount ?: 0
            binding.viewPager.currentItem = if (currentItem == totalItems - 1) 0 else currentItem + 1
            sliderHandler.postDelayed(sliderRunnable, 3000)
        }

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, 3000)
            }
        })
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, 3000)
    }
}
