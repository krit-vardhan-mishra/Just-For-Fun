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
import com.just_for_fun.justforfun.data.TVShows

class TVShowsAdapter(
    private val tvShows: List<TVShows>,
    private val itemClick: (TVShows) -> Unit
) : RecyclerView.Adapter<TVShowsAdapter.TVShowViewHolder>() {

    inner class TVShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: ImageView = itemView.findViewById(R.id.poster)
        val bookmark: AppCompatImageButton = itemView.findViewById(R.id.bookmark_button)

        init {
            poster.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    itemClick(tvShows[position])
                }
            }

            bookmark.setOnClickListener {
                Toast.makeText(itemView.context, "BookMarked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TVShowViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poster_box, parent, false)
        return TVShowViewHolder(view)
    }

    override fun onBindViewHolder(holder: TVShowViewHolder, position: Int) {
        val tvShow = tvShows[position]
        Glide.with(holder.itemView.context)
            .load(tvShow.posterUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.poster)

        holder.itemView.setOnClickListener {
            itemClick(tvShow)
        }
    }

    override fun getItemCount(): Int = tvShows.size
}
