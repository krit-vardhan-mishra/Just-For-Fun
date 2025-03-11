package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Movies

class MoviesAdapter(
    private val movies: List<Movies>,
    private val itemClick: (Movies) -> Unit
) : RecyclerView.Adapter<MoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val bookmark: AppCompatImageButton = itemView.findViewById(R.id.bookmark_button)

        init {
            poster.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    itemClick(movies[position])
                }
            }

            bookmark.setOnClickListener {
                Toast.makeText(itemView.context, "BookMarked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poster_box, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        Glide.with(holder.itemView.context)
            .load(movie.posterUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.poster)

        holder.itemView.setOnClickListener {
            itemClick(movie)
        }
    }

    override fun getItemCount(): Int = movies.size
}