package com.just_for_fun.justforfun.ui.fragments.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.ImageSliderAdapter
import com.just_for_fun.justforfun.adapters.MoviesAdapter
import com.just_for_fun.justforfun.adapters.TVShowsAdapter
import com.just_for_fun.justforfun.databinding.FragmentHomeBinding
import com.just_for_fun.justforfun.ui.fragments.details.MovieOrShowDetailsFragment

class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHomeBinding.bind(view)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        // Image slider setup
        val images = listOf(
            R.drawable.two, R.drawable.three, R.drawable.four,
            R.drawable.six, R.drawable.five, R.drawable.eight,
            R.drawable.nine, R.drawable.ten, R.drawable.seven,
            R.drawable.eleven, R.drawable.twelve, R.drawable.one,
            R.drawable.thirteen, R.drawable.fourteen, R.drawable.fifteen
        )

        val adapter = ImageSliderAdapter(requireContext(), images)
        binding.viewPager.adapter = adapter

        viewModel.movies.observe(viewLifecycleOwner) { movies ->
            binding.fragmentHomeMoviesRecyclerViewer.adapter = MoviesAdapter(movies) { movie ->
                val bundle = Bundle().apply {
                    putParcelable("MOVIE", movie)
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, MovieOrShowDetailsFragment().apply {
                        arguments = bundle
                    })
                    .addToBackStack(null)
                    .commit()
            }
        }

        viewModel.tvShows.observe(viewLifecycleOwner) { tvShows ->
            binding.fragmentHomeShowsRecyclerViewer.adapter = TVShowsAdapter(tvShows) { tvShow ->
                val bundle = Bundle().apply {
                    putParcelable("TV_SHOW", tvShow)
                }
                parentFragmentManager.beginTransaction()
                    .replace(R.id.nav_host_fragment, MovieOrShowDetailsFragment().apply {
                        arguments = bundle
                    })
                    .addToBackStack(null)
                    .commit()
            }
        }

        setupAutoSlider()
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

    override fun onDestroyView() {
        super.onDestroyView()
        sliderHandler.removeCallbacks(sliderRunnable)
        _binding = null
    }
}
