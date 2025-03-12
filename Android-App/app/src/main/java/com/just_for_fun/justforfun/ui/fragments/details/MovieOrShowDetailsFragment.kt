package com.just_for_fun.justforfun.ui.fragments.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Movies
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.databinding.FragmentMovieOrShowDetailsBinding

class MovieOrShowDetailsFragment : Fragment(R.layout.fragment_movie_or_show_details) {

    private lateinit var binding: FragmentMovieOrShowDetailsBinding
    private lateinit var viewModel: MovieOrShowDetailsViewModel

    private var movie: Movies? = null
    private var tvShow: TVShows? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movie = it.getParcelable(ARG_MOVIE)
            tvShow = it.getParcelable(ARG_TVSHOW)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieOrShowDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMovieOrShowDetailsBinding.bind(view)

        viewModel = ViewModelProvider(this)[MovieOrShowDetailsViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        movie?.let { displayMovieDetails(it) }
        tvShow?.let { displayTvShowDetails(it) }
    }

    private fun displayMovieDetails(movie: Movies) {
        binding.detailsTitle.text = movie.title
        binding.detailsDescription.text = movie.description
        Glide.with(this)
            .load(movie.posterUrl)
            .into(binding.detailsPoster)
    }

    private fun displayTvShowDetails(tvShow: TVShows) {
        binding.detailsTitle.text = tvShow.title
        binding.detailsDescription.text = tvShow.description
        Glide.with(this)
            .load(tvShow.posterUrl)
            .into(binding.detailsPoster)
    }

    companion object {
        private const val ARG_MOVIE = "arg_movie"
        private const val ARG_TVSHOW = "arg_tvshow"

        fun newInstance(movie: Movies): MovieOrShowDetailsFragment {
            return MovieOrShowDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_MOVIE, movie)
                }
            }
        }

        fun newInstance(tvShow: TVShows): MovieOrShowDetailsFragment {
            return MovieOrShowDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_TVSHOW, tvShow)
                }
            }
        }
    }
}