package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.util.Log
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.items.MovieItem
import com.just_for_fun.justforfun.util.SimpleDiffCallback

class SimilarMoviesAdapter(
    private val onMovieClick: (MovieItem) -> Unit
) : ListAdapter<MovieItem, SimilarMoviesAdapter.MovieViewHolder>(
    SimpleDiffCallback(
        areItemsSame = { old, new -> old.posterUrl == new.posterUrl },
        areContentsSame = { old, new -> old == new }
    )
) {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.similar_movie_poster)
        val title: TextView = itemView.findViewById(R.id.similar_movie_title)

        init {
            itemView.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    onMovieClick(getItem(position))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_similar_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = getItem(position)
        Log.d("SimilarMoviesAdapter", "Binding Movie: Title = ${movie.title}, Poster URL = ${movie.posterUrl}")
        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.poster)
        holder.title.text = movie.title
        Log.d("SimilarMoviesAdapter", "Text set to TextView: ${holder.title.text}")
    }
}
