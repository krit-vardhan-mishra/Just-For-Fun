package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R

class PosterAdapter(
    private var posterItems: List<Int>,
    private val onPosterClick: (Int) -> Unit,
    private val onBookmarkClick: (Int) -> Unit
) : RecyclerView.Adapter<PosterAdapter.PosterViewHolder>() {

    inner class PosterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val poster: AppCompatImageButton = itemView.findViewById(R.id.poster)
        val bookmark: AppCompatImageButton = itemView.findViewById(R.id.bookmark_button)

        init {
            poster.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    onPosterClick(position)
                }
            }
            bookmark.setOnClickListener {
                bindingAdapterPosition.takeIf { it != RecyclerView.NO_POSITION }?.let { position ->
                    onBookmarkClick(position)
                }
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
        val imageResId = posterItems[position]
        Glide.with(holder.itemView.context)
            .load(imageResId)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.poster)
    }
}
