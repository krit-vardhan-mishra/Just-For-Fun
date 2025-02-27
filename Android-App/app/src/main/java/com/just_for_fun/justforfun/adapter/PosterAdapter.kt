package com.just_for_fun.justforfun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R

class PosterAdapter(
    private val posterItems: List<Int>,
    private val onPosterClick: (Int) -> Unit,
    private val onBookmarkClick: (Int) -> Unit
) : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {

    inner class PosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: AppCompatImageButton = itemView.findViewById(R.id.poster)
        val bookmark: AppCompatImageButton = itemView.findViewById(R.id.bookmark_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.poster_box, parent, false)
        return PosterViewHolder(view)
    }

    override fun getItemCount(): Int = posterItems.size

    override fun onBindViewHolder(holder: PosterViewHolder, position: Int) {
        val imageRes = posterItems[position]

        holder.poster.setImageResource(imageRes)

        holder.poster.setOnClickListener { onPosterClick(imageRes) }
        holder.bookmark.setOnClickListener { onBookmarkClick(imageRes) }
    }
}