package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R

class PosterAdapter(
    private val posterItems: List<String>,
    private val onPosterClick: (Int) -> Unit,
    private val onBookmarkClick: (Int) -> Unit
) : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {

    inner class PosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: AppCompatImageButton = itemView.findViewById(R.id.poster)
        val bookmark: AppCompatImageButton = itemView.findViewById(R.id.bookmark_button)

        init {
            poster.setOnClickListener {
                onPosterClick(adapterPosition)
            }
            bookmark.setOnClickListener {
                onBookmarkClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poster_box, parent, false)
        return PosterViewHolder(view)
    }

    override fun getItemCount(): Int = posterItems.size

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val imageUrl = posterItems[position]

        Glide.with(holder.itemView.context)
            .load(imageUrl)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.poster)
    }
}