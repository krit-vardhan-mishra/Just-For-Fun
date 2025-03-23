package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.util.SimpleDiffCallback

class ImageSliderAdapter :
    ListAdapter<Int, ImageSliderAdapter.ViewHolder>(
        SimpleDiffCallback<Int>(
            areItemsSame = { old, new -> old == new }
        )
    ) {

    class ViewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.slide_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Calculate the actual position using modulo arithmetic
        val itemCount = currentList.size
        if (itemCount > 0) {
            val actualPosition = position % itemCount
            val imageRes = currentList[actualPosition]

            Glide.with(holder.itemView)
                .load(imageRes)
                .into(holder.imageView)
        }
    }

    // Override getItemCount to simulate an infinite list.
    override fun getItemCount(): Int = Int.MAX_VALUE
}
