package com.just_for_fun.justforfun.ui.fragments.movie.review

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.ReviewsAdapter
import com.just_for_fun.justforfun.databinding.FragmentReviewBinding
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class Reviews : Fragment(R.layout.fragment_review) {
    private val binding by viewBinding(FragmentReviewBinding::bind)
    private val viewModel: ReviewViewModel by viewModel()
    private val adapter = ReviewsAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupRecyclerView()
        setupReviewPosting()
        observeViewModel()
    }

    private fun observeViewModel() {
        Log.d("ReviewsFragment", "Setting up observer")
        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            Log.d("ReviewsFragment", "Received ${reviews.size} reviews from ViewModel")
            adapter.submitList(reviews ?: emptyList())
        }
    }

    private fun setupRecyclerView() {
        binding.activityMovieOthersReview.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@Reviews.adapter
            isNestedScrollingEnabled = false
        }
    }

    private fun setupReviewPosting() {
        binding.postReviewButton.setOnClickListener {
            val reviewText = binding.yourReviewEditText.text.toString().trim()
            if (reviewText.isNotEmpty()) {
                viewModel.postReview(reviewText)
                binding.yourReviewEditText.text?.clear()
            }
        }
    }
}