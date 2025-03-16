package com.just_for_fun.justforfun.ui.fragments.movie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.CastCrewAdapter
import com.just_for_fun.justforfun.adapters.ReviewsAdapter
import com.just_for_fun.justforfun.adapters.SimilarMoviesAdapter
import com.just_for_fun.justforfun.data.CastCrewMember
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.databinding.FragmentMovieOrShowsBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.PosterItemDecoration
import com.just_for_fun.justforfun.util.delegates.viewBinding
import com.just_for_fun.justforfun.util.deserializer.CastCrewDeserializer
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.net.toUri

class MovieFragment : Fragment(R.layout.fragment_movie_or_shows) {

    private val binding by viewBinding(FragmentMovieOrShowsBinding::bind)
    private val viewModel: MovieViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        val title = arguments?.getString("MOVIE_TITLE") ?: "Unknown Movie"
        val posterUrl = arguments?.getInt("MOVIE_POSTER") ?: R.drawable.fallback_poster
        val description = arguments?.getString("MOVIE_DESCRIPTION") ?: "No description available"
        val rating = arguments?.getFloat("MOVIE_RATING") ?: 0.0f
        val type = arguments?.getString("MOVIE_TYPE") ?: "Movie"

        binding.activityMovieTitle.text = title
        binding.activityMovieMovieOrShow.text = type
        Glide.with(this)
            .load(posterUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(binding.activityMoviePoster)
        binding.activityMovieDescription.text = description
        binding.activityMovieRatingBar.rating = rating
        binding.activityMovieUserRating.text = "$rating/5"

        binding.floatingActionButton.setOnClickListener {
            openTelegramLink("https://t.me/JustForFunGroup_04")
        }

        setupMovieDetails()
        observeViewModel()
    }


    private fun observeViewModel() {
        viewModel.selectedCastMember.observe(viewLifecycleOwner) { castMember ->
            castMember?.let {
                findNavController().navigate(R.id.nav_celebrityFragment)
                viewModel.onCastMemberNavigated()
            }
        }

        viewModel.testCases.observe(viewLifecycleOwner) { testCases ->
            if (testCases != null) {
                val type = arguments?.getString("MOVIE_TYPE") ?: "Movie"
                viewModel.setupMoreLikeThis(type)
            }
        }

        viewModel.similarMovies.observe(viewLifecycleOwner) { similarMovies ->
            setupMoreLikeThis(similarMovies)
        }

        viewModel.reviews.observe(viewLifecycleOwner) { reviews ->
            reviews?.let { setupReviews(it) }
        }
    }

    private fun setupMovieDetails() {
        setupCastAndCrew()
    }

    private fun setupCastAndCrew() {
        val castAndCrew = loadCastAndCrewFromJson()
        val castAdapter = CastCrewAdapter(castAndCrew) { member ->
            viewModel.selectCastMember(member)
        }

        binding.movieActivityCastAndCrew.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            addItemDecoration(PosterItemDecoration(15))
        }
    }

    private fun loadCastAndCrewFromJson(): List<CastCrewMember> {
        return try {
            val jsonString = requireContext().assets.open("cast_crew.json")
                .bufferedReader().use { it.readText() }

            val gson = GsonBuilder()
                .registerTypeAdapter(
                    CastCrewMember::class.java,
                    CastCrewDeserializer(requireContext())
                )
                .create()

            val listType = object : TypeToken<List<CastCrewMember>>() {}.type

            gson.fromJson(jsonString, listType)
        } catch (e: Exception) {
            Log.d("MovieFragment", "Error loading cast and crew: ${e.message}")
            emptyList()
        }
    }

    private fun setupMoreLikeThis(similarMovies: List<MovieItem>) {
        val similarMoviesAdapter = SimilarMoviesAdapter(similarMovies) { movie ->
            val args = Bundle().apply {
                putString("MOVIE_TITLE", movie.title)
                putInt("MOVIE_POSTER", movie.posterUrl)
                putString("MOVIE_DESCRIPTION", movie.description)
                putFloat("MOVIE_RATING", movie.rating)
                putString("MOVIE_TYPE", movie.type)
            }
            findNavController().navigate(R.id.nav_movieFragment, args)
        }

        binding.movieActivityMoreLikeThis.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = similarMoviesAdapter
            addItemDecoration(PosterItemDecoration(15))
        }
    }

    private fun setupReviews(reviews: List<Review>) {
        val reviewsAdapter = ReviewsAdapter(reviews)
        binding.activityMovieOthersReview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = reviewsAdapter
            isNestedScrollingEnabled = false
        }

        binding.root.findViewById<ImageButton>(R.id.post_review_button)?.setOnClickListener {
            val reviewEditText = binding.root.findViewById<EditText>(R.id.your_review_edit_text)
            val reviewText = reviewEditText?.text.toString()

            if (reviewText.isNotEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Review submitted: $reviewText",
                    Toast.LENGTH_SHORT
                ).show()
                reviewEditText?.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please write a review first", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun openTelegramLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, url.toUri())
        startActivity(intent)
    }
}
