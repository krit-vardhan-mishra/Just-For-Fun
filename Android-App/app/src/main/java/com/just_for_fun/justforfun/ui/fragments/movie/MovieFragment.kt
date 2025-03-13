package com.just_for_fun.justforfun.ui.fragments.movie

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.CastAndCrewAdapter
import com.just_for_fun.justforfun.adapters.ReviewsAdapter
import com.just_for_fun.justforfun.adapters.SimilarMoviesAdapter
import com.just_for_fun.justforfun.data.CastCrewMember
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.databinding.FragmentMovieOrShowsBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.SpacingItemDecoration
import java.util.Date

class MovieFragment : Fragment(R.layout.fragment_movie_or_shows) {

    private lateinit var binding: FragmentMovieOrShowsBinding
    private lateinit var viewModel: MovieViewModel

    private val similarMovies = listOf(
        MovieItem(R.drawable.mm_poster, "Mr & Mrs. 55", "Mr & Mrs. 55 is a classic romantic comedy", 4.1f, "Comedy"),
        MovieItem(R.drawable.lagaan_poster, "Lagaan", "Lagaan is story of villager who play cricket against the Britisher to get lease on their lagaan", 4.3f, "Drama"),
        MovieItem(R.drawable.kranti_poster, "Kranti", "Description 3", 3.9f, "Action"),
        MovieItem(R.drawable.ddlj_poster, "Dilwale Dulhaniya Le Jayenge", "Description 4", 4.5f, "Romance")
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieOrShowsBinding.bind(view)

        viewModel = ViewModelProvider(this)[MovieViewModel::class.java]
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
        binding.activityMovieUserRating.text = "${rating}/5"

        setupMovieDetails()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.selectedCastMember.observe(viewLifecycleOwner) { castMember ->
            castMember?.let {
                val args = Bundle().apply {
                    putString("CELEBRITY_NAME", it.name)
                    putString("CELEBRITY_ROLE", it.role)
                    putInt("CELEBRITY_IMAGE", it.id)
                }
                findNavController().navigate(R.id.nav_celebrityFragment, args)
                viewModel.onCastMemberNavigated()
            }
        }
    }

    private fun setupMovieDetails() {
        setupCastAndCrew()
        setupMoreLikeThis()
        setupReviews()
    }

    private fun setupCastAndCrew() {
        val castAndCrew = listOf(
            CastCrewMember(R.drawable.cast_one, "Actor Name 1", "Character 1", "ROLE 1"),
            CastCrewMember(R.drawable.cast_two, "Actor Name 2", "Character 2", "ROLE 2"),
            CastCrewMember(R.drawable.cast_three, "Director Name", "Director", "ROLE 3"),
            CastCrewMember(R.drawable.cast_four, "Producer Name", "Producer", "ROLE 4"),
            CastCrewMember(R.drawable.cast_five, "Writer Name", "Writer", "ROLE 5")
        )

        val castAdapter = CastAndCrewAdapter(castAndCrew) { member ->
            viewModel.selectCastMember(member)
        }

        binding.movieActivityCastAndCrew.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            addItemDecoration(SpacingItemDecoration(15))
        }
    }

    private fun setupMoreLikeThis() {
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
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = similarMoviesAdapter
            addItemDecoration(SpacingItemDecoration(15))
        }
    }

    private fun setupReviews() {
        val reviews = listOf(
            Review(
                id = "1",
                username = "User 1",
                avatarResId = R.drawable.account_circle,
                comment = "Great movie! Loved the storyline and acting.",
                rating = 4.5f,
                date = Date(),
                likeCount = 10,
                isLiked = true,
                replies = mutableListOf(
                    Reply(
                        id = "1-1",
                        username = "User 1",
                        comment = "Thank you!",
                        date = Date(System.currentTimeMillis()),
                        avatarResId = R.drawable.account_circle,
                        likedCount = 2,
                        isLiked = true
                    )
                )
            ),
            Review(
                id = "2",
                username = "User 2",
                avatarResId = R.drawable.account_circle,
                comment = "Good film but the ending could have been better.",
                rating = 3.8f,
                date = Date(),
                likeCount = 5,
                isLiked = false,
                replies = mutableListOf()
            ),
            Review(
                id = "3",
                username = "User 3",
                avatarResId = R.drawable.account_circle,
                comment = "Classic! One of my favorites of all time.",
                rating = 5.0f,
                date = Date(),
                likeCount = 20,
                isLiked = true,
                replies = mutableListOf()
            )
        )

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
                Toast.makeText(requireContext(), "Review submitted: $reviewText", Toast.LENGTH_SHORT).show()
                reviewEditText?.text?.clear()
            } else {
                Toast.makeText(requireContext(), "Please write a review first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}