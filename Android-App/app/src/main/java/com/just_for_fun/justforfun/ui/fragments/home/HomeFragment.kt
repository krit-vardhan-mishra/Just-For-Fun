package com.just_for_fun.justforfun.ui.fragments.home

import androidx.fragment.app.viewModels
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapter.ImageSliderAdapter
import com.just_for_fun.justforfun.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var sliderHandler: Handler
    private lateinit var sliderRunnable: Runnable

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val images = listOf(
            R.drawable.two, R.drawable.three, R.drawable.four,
            R.drawable.six, R.drawable.five, R.drawable.eight,
            R.drawable.nine, R.drawable.ten, R.drawable.seven,
            R.drawable.eleven, R.drawable.twelve, R.drawable.one,
            R.drawable.thirteen, R.drawable.fourteen, R.drawable.fifteen
        )

        val adapter = ImageSliderAdapter(requireContext(), images)
        binding.viewPager.adapter = adapter

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

        binding.viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
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

    override fun onDestroy() {
        super.onDestroy()
        sliderHandler.removeCallbacks(sliderRunnable)
    }
}