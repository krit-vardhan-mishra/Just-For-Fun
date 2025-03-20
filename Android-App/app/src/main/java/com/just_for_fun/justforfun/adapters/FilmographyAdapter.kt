package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.items.MovieItem

class FilmographyAdapter : ListAdapter<MovieItem, FilmographyAdapter.FilmographyHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilmographyHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_celebrity_filmography, parent, false)
        return FilmographyHolder(view)
    }

    override fun onBindViewHolder(holder: FilmographyHolder, position: Int) {
        // No binding logic needed for now
    }

    class FilmographyHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // No view bindings or logic for now
    }

    class DiffCallback : DiffUtil.ItemCallback<MovieItem>() {
        override fun areItemsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem.posterUrl == newItem.posterUrl
        }

        override fun areContentsTheSame(oldItem: MovieItem, newItem: MovieItem): Boolean {
            return oldItem == newItem
        }
    }
}