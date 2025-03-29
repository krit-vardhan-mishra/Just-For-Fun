package com.just_for_fun.justforfun.ui.fragments.account

import android.os.Bundle
import android.util.Log
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.View
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.PosterAdapter
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.databinding.FragmentAccountBinding
import com.just_for_fun.justforfun.util.decoration.SpaceItemDecoration
import com.just_for_fun.justforfun.util.delegates.viewBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private val binding by viewBinding(FragmentAccountBinding::bind)
    private val viewModel: AccountViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.nav_settingsFragment)
        }

        binding.fragmentAccountFavRecyclerView.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.HORIZONTAL, false)
        binding.fragmentAccountFavRecyclerView.addItemDecoration(SpaceItemDecoration(15))

        val posterAdapter = PosterAdapter(
            onPosterClick = { position ->
                // Determine if clicked item is from movies or TV shows
                viewModel.combinedContent.value ?: return@PosterAdapter
                val allMovies = viewModel.movies.value ?: emptyList()
                val allTvShows = viewModel.tvShows.value ?: emptyList()

                if (position < allMovies.size) {
                    // It's a movie
                    val movie = allMovies[position]
                    onMovieClick(movie)
                } else {
                    // It's a TV show
                    val tvShowPosition = position - allMovies.size
                    val tvShow = allTvShows[tvShowPosition]
                    onTVShowClick(tvShow)
                }
            },
            onBookmarkClick = { position ->
                Toast.makeText(requireContext(), "Bookmarked", Toast.LENGTH_SHORT).show()
            }
        )

        binding.fragmentAccountFavRecyclerView.adapter = posterAdapter

        viewModel.combinedContent.observe(viewLifecycleOwner) { combinedList ->
            Log.d("AccountFragment", "Combine Content List: ${combinedList.size}")
            posterAdapter.submitList(combinedList)
        }
    }

    private fun onMovieClick(movie: Movies) {
        val bundle = bundleOf(
            "MOVIE_TITLE" to movie.title,
            "MOVIE_POSTER" to movie.posterUrl,
            "MOVIE_DESCRIPTION" to movie.description,
            "MOVIE_RATING" to movie.rating,
            "MOVIE_TYPE" to "Movie"
        )
        findNavController().navigate(R.id.nav_movieFragment, bundle)
    }

    private fun onTVShowClick(tvShow: TVShows) {
        val bundle = bundleOf(
            "TV_SHOW_TITLE" to tvShow.title,
            "TV_SHOW_POSTER" to tvShow.posterUrl,
            "TV_SHOW_DESCRIPTION" to tvShow.description,
            "TV_SHOW_RATING" to tvShow.rating,
            "TV_SHOW_TYPE" to "TV Show"
        )
        findNavController().navigate(R.id.nav_movieFragment, bundle)
    }
}
