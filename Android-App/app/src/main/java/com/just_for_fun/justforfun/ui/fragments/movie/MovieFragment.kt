package com.just_for_fun.justforfun.ui.fragments.movie

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
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
import com.just_for_fun.justforfun.ui.fragments.celebrity.CelebrityActivity
import com.just_for_fun.justforfun.util.SpacingItemDecoration
import java.util.Date

class MovieFragment : Fragment(R.layout.fragment_movie_or_shows) {

    private lateinit var binding: FragmentMovieOrShowsBinding
    private val viewModel: MovieViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieOrShowsBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val title = requireActivity().intent.getStringExtra("MOVIE_TITLE") ?: "Unknown Movie"
        val posterUrl = requireActivity().intent.getStringExtra("MOVIE_POSTER")
        val description = requireActivity().intent.getStringExtra("MOVIE_DESCRIPTION") ?: "No description available"
        val rating = requireActivity().intent.getFloatExtra("MOVIE_RATING", 0.0f)
        val type = requireActivity().intent.getStringExtra("MOVIE_TYPE") ?: "Movie"

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
                val intent = Intent(requireContext(), CelebrityActivity::class.java).apply {
                    putExtra("CELEBRITY_NAME", it.name)
                    putExtra("CELEBRITY_ROLE", it.role)
                    putExtra("CELEBRITY_IMAGE", it.id)
                }
                startActivity(intent)
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
        val similarMovies = listOf(
            MovieItem(R.drawable.mm_poster, "Mr & Mrs. 55", "Mr & Mrs. 55 is a classic romantic comedy", 4.1f, "Comedy"),
            MovieItem(R.drawable.lagaan_poster, "Lagaan", "Lagaan is story of villager who play cricket against the Britisher to get lease on their lagaan", 4.3f, "Drama"),
            MovieItem(R.drawable.kranti_poster, "Kranti", "Description 3", 3.9f, "Action"),
            MovieItem(R.drawable.ddlj_poster, "Dilwale Dulhaniya Le Jayenge", "Description 4", 4.5f, "Romance")
        )

        val similarMoviesAdapter = SimilarMoviesAdapter(similarMovies) { movie ->
            Toast.makeText(requireContext(), "Selected: ${movie.title}", Toast.LENGTH_SHORT).show()

            val intent = Intent(requireContext(), requireActivity()::class.java).apply {
                putExtra("MOVIE_TITLE", movie.title)
                putExtra("MOVIE_POSTER", movie.posterUrl)
                putExtra("MOVIE_DESCRIPTION", movie.description)
                putExtra("MOVIE_RATING", movie.rating)
                putExtra("MOVIE_TYPE", movie.type)
            }
            startActivity(intent)
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