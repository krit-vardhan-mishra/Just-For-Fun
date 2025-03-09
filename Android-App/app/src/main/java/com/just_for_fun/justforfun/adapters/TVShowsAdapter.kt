package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.TVShows

class TVShowsAdapter(
    private val tvShows: List<TVShows>,
    private val itemClick: (TVShows) -> Unit
) : RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder>() {

    inner class TVShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.similar_movie_poster)
        val title: TextView = itemView.findViewById(R.id.similar_movie_title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_similar_movie, parent, false)
        return TVShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        val tvShow = tvShows[position]
        Glide.with(holder.itemView.context)
            .load(tvShow.posterUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.poster)
        holder.title.text = tvShow.title

        holder.itemView.setOnClickListener {
            itemClick(tvShow)
        }
    }

    override fun getItemCount(): Int = tvShows.size
}
