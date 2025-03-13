package com.just_for_fun.justforfun.ui.fragments.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.ImageSliderAdapter
import com.just_for_fun.justforfun.adapters.MoviesAdapter
import com.just_for_fun.justforfun.adapters.TVShowsAdapter
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.databinding.FragmentHomeBinding
import kotlin.math.abs

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable
    private val sliderInterval = 3000L
    private val indicators = mutableListOf<ImageView>()

    private val images = listOf(
        R.drawable.two, R.drawable.three, R.drawable.four,
        R.drawable.six, R.drawable.five, R.drawable.eight,
        R.drawable.nine, R.drawable.ten, R.drawable.seven,
        R.drawable.eleven, R.drawable.twelve, R.drawable.one,
        R.drawable.thirteen, R.drawable.fourteen, R.drawable.fifteen
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupViewPager()
        setupIndicators()
        setCurrentIndicator(0)
        setupMoviesRecyclerView()
        setupTVShowsRecyclerView()
        setupAutoSlider()
    }

    private fun setupViewPager() {
        val sliderAdapter = ImageSliderAdapter(images)
        binding.viewPager.adapter = sliderAdapter

        val pageMarginPx = resources.getDimensionPixelOffset(R.dimen.page_margin)
        val offsetPx = resources.getDimensionPixelOffset(R.dimen.page_offset)
        binding.viewPager.setPadding(offsetPx, 0, offsetPx, 0)
        binding.viewPager.clipToPadding = false
        binding.viewPager.clipChildren = false

        val middlePosition = Int.MAX_VALUE / 2 - (Int.MAX_VALUE / 2) % images.size
        binding.viewPager.setCurrentItem(middlePosition, false)
        binding.viewPager.offscreenPageLimit = 3

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(pageMarginPx))
        compositePageTransformer.addTransformer { page, position ->
            val scaleFactor = 0.85f + (1 - abs(position)) * 0.15f
            page.scaleY = scaleFactor
            page.scaleX = scaleFactor
        }

        binding.viewPager.setPageTransformer(compositePageTransformer)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setCurrentIndicator(position % images.size)
            }
        })
    }

    private fun setupIndicators() {
        binding.indicatorContainer.removeAllViews()
        indicators.clear()

        for (i in images.indices) {
            val indicator = ImageView(requireContext())
            val layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(8, 0, 8, 0)
            indicator.layoutParams = layoutParams
            indicator.setImageDrawable(
                ContextCompat.getDrawable(requireContext(), R.drawable.indicator_inactive)
            )
            binding.indicatorContainer.addView(indicator)
            indicators.add(indicator)
        }
    }

    private fun setCurrentIndicator(position: Int) {
        for (i in indicators.indices) {
            val drawable = if (i == position) R.drawable.indicator_active else R.drawable.indicator_inactive
            indicators[i].setImageDrawable(ContextCompat.getDrawable(requireContext(), drawable))
        }
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
            sliderHandler.postDelayed(sliderRunnable, sliderInterval)
        }
        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                sliderHandler.removeCallbacks(sliderRunnable)
                sliderHandler.postDelayed(sliderRunnable, sliderInterval)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        sliderHandler.postDelayed(sliderRunnable, sliderInterval)
    }

    override fun onPause() {
        super.onPause()
        sliderHandler.removeCallbacks(sliderRunnable)
    }
}