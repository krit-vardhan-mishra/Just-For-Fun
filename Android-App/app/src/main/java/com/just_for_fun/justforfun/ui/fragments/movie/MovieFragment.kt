package com.just_for_fun.justforfun.ui.fragments.movie

import android.content.Intent
import android.net.Uri
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
import com.just_for_fun.justforfun.adapters.CastAndCrewAdapter
import com.just_for_fun.justforfun.adapters.ReviewsAdapter
import com.just_for_fun.justforfun.adapters.SimilarMoviesAdapter
import com.just_for_fun.justforfun.data.CastCrewMember
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.databinding.FragmentMovieOrShowsBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.PosterItemDecoration
import com.just_for_fun.justforfun.util.delegates.viewBinding
import com.just_for_fun.justforfun.util.deserializer.ReplyDeserializer
import com.just_for_fun.justforfun.util.deserializer.ReviewDeserializer
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class MovieFragment : Fragment(R.layout.fragment_movie_or_shows) {

    private val binding by viewBinding(FragmentMovieOrShowsBinding::bind)
    private val viewModel: MovieViewModel by viewModel()

    private val similarMovies = listOf(
        MovieItem(
            R.drawable.mm_poster,
            "Mr & Mrs. 55",
            "Mr & Mrs. 55 is a classic romantic comedy",
            4.1f,
            "Comedy"
        ),
        MovieItem(
            R.drawable.lagaan_poster,
            "Lagaan",
            "Lagaan is story of villager who play cricket against the Britisher to get lease on their lagaan",
            4.3f,
            "Drama"
        ),
        MovieItem(R.drawable.kranti_poster, "Kranti", "Description 3", 3.9f, "Action"),
        MovieItem(
            R.drawable.ddlj_poster,
            "Dilwale Dulhaniya Le Jayenge",
            "Description 4",
            4.5f,
            "Romance"
        )
    )

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
        binding.activityMovieUserRating.text = "${rating}/5"

        binding.floatingActionButton.setOnClickListener {
            openTelegramLink("https://t.me/JustForFunGroup_04")
        }

        setupMovieDetails()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.selectedCastMember.observe(viewLifecycleOwner) { castMember ->
            castMember?.let {
                val args = Bundle().apply {
                    putString("CELEBRITY_NAME", it.name)
                    putString("CELEBRITY_ROLE", it.role)
                    putString("CELEBRITY_IMAGE", it.id)
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
            CastCrewMember("1", R.drawable.cast_one, "Madhubala", "Actress"),
            CastCrewMember("2", R.drawable.cast_two, "GuruDutt", "Actor"),
            CastCrewMember("3", R.drawable.cast_three, "Manthara", "Actress"),
            CastCrewMember("4", R.drawable.cast_four, "Johny Walker", "Producer"),
            CastCrewMember("5", R.drawable.cast_five, "TunTun-Massi", "Writer")
        )

        val castAdapter = CastAndCrewAdapter(castAndCrew) { member ->
            viewModel.selectCastMember(member)
        }

        binding.movieActivityCastAndCrew.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = castAdapter
            addItemDecoration(PosterItemDecoration(15))
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
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = similarMoviesAdapter
            addItemDecoration(PosterItemDecoration(15))
        }
    }

    private fun setupReviews() {
        try {
            val assetManager = requireActivity().application.assets
            val inputStream = assetManager.open("reviews.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = GsonBuilder()
                .registerTypeAdapter(
                    Review::class.java,
                    ReviewDeserializer(requireActivity().application)
                )
                .registerTypeAdapter(
                    Reply::class.java,
                    ReplyDeserializer(requireActivity().application)
                )
                .create()

            val type = object : TypeToken<List<Review>>() {}.type
            val reviews: List<Review> = gson.fromJson(jsonString, type)

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
                    Toast.makeText(
                        requireContext(),
                        "Please write a review first",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } catch (e: IOException) {
            Log.e("MovieFragment", "Error reading reviews JSON", e)
        } catch (e: Exception) {
            Log.e("MovieFragment", "Error parsing reviews JSON", e)
        }
    }

    private fun openTelegramLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}