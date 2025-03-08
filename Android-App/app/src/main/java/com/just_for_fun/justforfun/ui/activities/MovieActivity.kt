package com.just_for_fun.justforfun.ui.activities

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapter.CastAndCrewAdapter
import com.just_for_fun.justforfun.adapter.ReviewsAdapter
import com.just_for_fun.justforfun.adapter.SimilarMoviesAdapter
import com.just_for_fun.justforfun.data.CastCrewMember
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.databinding.ActivityMovieBinding
import com.just_for_fun.justforfun.databinding.ActivityMovieOrShowsBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.SpacingItemDecoration
import java.util.Date

class MovieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMovieOrShowsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMovieBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra("MOVIE_TITLE") ?: "Unknown Movie"
        val posterResId = intent.getIntExtra("MOVIE_POSTER", R.drawable.mm_poster)
        val description = intent.getStringExtra("MOVIE_DESCRIPTION") ?: "No description available"
        val rating = intent.getFloatExtra("MOVIE_RATING", 0.0f)
        val type = intent.getStringExtra("MOVIE_TYPE") ?: "Movie"

        binding.activityMovieTitle.text = title
        binding.activityMovieMovieOrShow.text = type
        binding.activityMoviePoster.setImageResource(posterResId)
        binding.activityMovieDescription.text = description
        binding.activityMovieRatingBar.rating = rating
        binding.activityMovieUserRating.text = "${rating}/5"

        setupMovieDetails()
    }

    private fun setupMovieDetails() {
        setupCastAndCrew()
        setupMoreLikeThis()
        setupReviews()
    }

    private fun setupCastAndCrew() {
        // Sample cast and crew data
        val castAndCrew = listOf(
            CastCrewMember(R.drawable.cast_one, "Actor Name 1", "Character 1"),
            CastCrewMember(R.drawable.cast_two, "Actor Name 2", "Character 2"),
            CastCrewMember(R.drawable.cast_three, "Director Name", "Director"),
            CastCrewMember(R.drawable.cast_four, "Producer Name", "Producer"),
            CastCrewMember(R.drawable.cast_five, "Writer Name", "Writer")
        )

        val castAdapter = CastAndCrewAdapter(castAndCrew)
        binding.movieActivityCastAndCrew.apply {
            layoutManager = LinearLayoutManager(this@MovieActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            addItemDecoration(SpacingItemDecoration(15))
        }
    }

    private fun setupMoreLikeThis() {
        // Sample similar movies
        val similarMovies = listOf(
            MovieItem(R.drawable.mm_poster, "Similar Movie 1", "Description 1", 4.1f, "Movie"),
            MovieItem(R.drawable.lagaan_poster, "Similar Movie 2", "Description 2", 4.3f, "Movie"),
            MovieItem(R.drawable.kranti_poster, "Similar Movie 3", "Description 3", 3.9f, "Movie"),
            MovieItem(R.drawable.ddlj_poster, "Similar Movie 4", "Description 4", 4.5f, "Movie")
        )

        val similarMoviesAdapter = SimilarMoviesAdapter(similarMovies) { movie ->
            // Handle click on similar movie
            Toast.makeText(this, "Selected: ${movie.title}", Toast.LENGTH_SHORT).show()

            // Option to navigate to the same activity with new movie data
            val intent = Intent(this, MovieActivity::class.java).apply {
                putExtra("MOVIE_TITLE", movie.title)
                putExtra("MOVIE_POSTER", movie.posterResId)
                putExtra("MOVIE_DESCRIPTION", movie.description)
                putExtra("MOVIE_RATING", movie.rating)
                putExtra("MOVIE_TYPE", movie.type)
            }
            startActivity(intent)
        }

        binding.movieActivityMoreLikeThis.apply {
            layoutManager = LinearLayoutManager(this@MovieActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = similarMoviesAdapter
            addItemDecoration(SpacingItemDecoration(15))
        }
    }

    private fun setupReviews() {
        // Sample reviews
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
            layoutManager = LinearLayoutManager(this@MovieActivity)
            adapter = reviewsAdapter
            isNestedScrollingEnabled = false
        }

        // Setup your review submission
        binding.root.findViewById<ImageButton>(R.id.post_review_button)?.setOnClickListener {
            val reviewEditText = binding.root.findViewById<EditText>(R.id.your_review_edit_text)
            val reviewText = reviewEditText?.text.toString()

            if (reviewText.isNotEmpty()) {
                Toast.makeText(this, "Review submitted: $reviewText", Toast.LENGTH_SHORT).show()
                reviewEditText?.text?.clear()

            } else {
                Toast.makeText(this, "Please write a review first", Toast.LENGTH_SHORT).show()
            }
        }
    }
}