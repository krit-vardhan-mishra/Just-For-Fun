package com.just_for_fun.justforfun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.items.MovieItem

class SimilarMoviesAdapter(
    private val movies: List<MovieItem>,
    private val onMovieClick: (MovieItem) -> Unit
) : RecyclerView.Adapter<SimilarMoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.similar_movie_poster)
        val title: TextView = itemView.findViewById(R.id.similar_movie_title)

        init {
            itemView.setOnClickListener {
                onMovieClick(movies[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_similar_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.poster.setImageResource(movie.posterResId)
        holder.title.text = movie.title
    }

    override fun getItemCount() = movies.size
}
