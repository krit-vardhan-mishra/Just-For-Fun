package com.just_for_fun.justforfun.ui.fragments.poster

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.PosterAdapter
import com.just_for_fun.justforfun.data.TVShows
import com.just_for_fun.justforfun.databinding.FragmentRecyclerViewersBinding
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.GridSpacingItemDecoration
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class PosterFragment : Fragment(R.layout.fragment_recycler_viewers) {

    private val binding by viewBinding(FragmentRecyclerViewersBinding::bind)
    private val viewModel: PostersViewModel by viewModel()

    val title: String? get() = arguments?.getString("title")
    val subtitle: String? get() = arguments?.getString("subtitle")
    val dataType: String? get() = arguments?.getString("dataType")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        viewModel.setToolbarData(title ?: "Default Title", subtitle ?: "Default Subtitle")
        viewModel.loadPosters(dataType ?: "movies")

        viewModel.posterList.observe(viewLifecycleOwner, Observer { posterIds ->
            val spanCount = calculateSpanCount()
            binding.recyclerViewPosters.apply {
                layoutManager = GridLayoutManager(context, spanCount)
                while (itemDecorationCount > 0) {
                    removeItemDecorationAt(0)
                }
                val spacingInPx = resources.getDimensionPixelSize(R.dimen.spacing)
                addItemDecoration(
                    GridSpacingItemDecoration(
                        spanCount = spanCount,
                        spacing = spacingInPx,
                        includeEdge = true
                    )
                )
                adapter = PosterAdapter(posterIds, onPosterClick = { position ->
                    val selectedPosterId = posterIds[position]
                    val movieData = getMovieOrShowData(selectedPosterId, dataType ?: "movies")

                    if (movieData != null) {
                        val args = Bundle().apply {
                            putString("MOVIE_TITLE", movieData.title)
                            putInt("MOVIE_POSTER", selectedPosterId)
                            putString("MOVIE_DESCRIPTION", movieData.description)
                            putFloat("MOVIE_RATING", movieData.rating)
                            putString("MOVIE_TYPE", movieData.type)
                            // Additional TV Show data if applicable:
                            if (movieData.type == "TV Show" && dataType == "tvShows") {
                                putBoolean("IS_TV_SHOW", true)
                                val tvShowData = getTVShowData(movieData.title)
                                tvShowData?.let {
                                    putInt("TV_SEASONS", it.totalSeasons)
                                    putInt("TV_EPISODES", it.totalEpisodes)
                                }
                            }
                        }
                        findNavController().navigate(R.id.nav_movieFragment, args)
                    } else {
                        Toast.makeText(requireContext(), "Error loading content details", Toast.LENGTH_SHORT).show()
                    }
                }, onBookmarkClick = { position ->
                    Toast.makeText(requireContext(), "Bookmark clicked", Toast.LENGTH_SHORT).show()
                })
            }
        })
    }

    private fun calculateSpanCount(): Int {
        val displayMetrics = resources.displayMetrics
        val screenWidthPx = displayMetrics.widthPixels
        val posterWidthPx = resources.getDimensionPixelSize(R.dimen.poster_width)
        return (screenWidthPx / posterWidthPx).coerceAtLeast(2)
    }

    private fun getMovieOrShowData(posterId: Int, dataType: String): MovieItem? {
        try {
            val assetManager = requireActivity().application.assets
            val inputStream = assetManager.open("sample_cases.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val array = if (dataType == "movies") {
                jsonObject.getAsJsonArray("movies")
            } else {
                jsonObject.getAsJsonArray("tvShows")
            }

            for (i in 0 until array.size()) {
                val item = array.get(i).asJsonObject
                val posterString = item.get("posterUrl").asString
                val resourceId = getResourceIdFromString(posterString)

                if (resourceId == posterId) {
                    return MovieItem(
                        posterId,
                        item.get("title").asString,
                        item.get("description").asString,
                        item.get("rating").asFloat,
                        item.get("type").asString
                    )
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("PosterFragment", "Error getting content data", e)
            return null
        }
    }

    private fun getTVShowData(title: String): TVShows? {
        try {
            val assetManager = requireActivity().application.assets
            val inputStream = assetManager.open("sample_cases.json")
            val jsonString = inputStream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val jsonObject = gson.fromJson(jsonString, JsonObject::class.java)
            val showsArray = jsonObject.getAsJsonArray("tvShows")

            for (i in 0 until showsArray.size()) {
                val show = showsArray.get(i).asJsonObject
                if (show.get("title").asString == title) {
                    val posterString = show.get("posterUrl").asString
                    val resourceId = getResourceIdFromString(posterString)
                    return TVShows(
                        resourceId,
                        show.get("title").asString,
                        show.get("description").asString,
                        show.get("rating").asFloat,
                        show.get("type").asString,
                        show.get("totalSeasons").asInt,
                        show.get("totalEpisodes").asInt,
                        show.get("showRunner").asString,
                        show.get("yearAired").asString
                    )
                }
            }
            return null
        } catch (e: Exception) {
            Log.e("PosterFragment", "Error getting TV show data", e)
            return null
        }
    }

    private fun getResourceIdFromString(drawableString: String): Int {
        val resourceName = drawableString.replace("R.drawable.", "")
        return resources.getIdentifier(
            resourceName, "drawable", requireActivity().packageName
        )
    }
}
