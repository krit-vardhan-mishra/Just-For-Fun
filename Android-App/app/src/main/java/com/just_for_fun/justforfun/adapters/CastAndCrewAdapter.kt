package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.CastCrewMember

class CastAndCrewAdapter(
    private val castAndCrew: List<CastCrewMember>,
    private val onItemClick: (CastCrewMember) -> Unit // Add click listener lambda
) : RecyclerView.Adapter<CastAndCrewAdapter.CastCrewViewHolder>() {

    inner class CastCrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cast_crew_image)
        val name: TextView = itemView.findViewById(R.id.cast_crew_name)
        val role: TextView = itemView.findViewById(R.id.cast_crew_role)

        // Bind click listener to the item view
        fun bind(member: CastCrewMember, onItemClick: (CastCrewMember) -> Unit) {
            itemView.setOnClickListener { onItemClick(member) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastCrewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cast_and_crew_layout, parent, false)
        return CastCrewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastCrewViewHolder, position: Int) {
        val member = castAndCrew[position]

        // Use Glide to load images
        Glide.with(holder.itemView.context)
            .load(member.image)
            .placeholder(R.drawable.placeholder_image)
            .error(R.drawable.error_image)
            .into(holder.image)

        holder.name.text = member.name
        holder.role.text = member.role

        // Bind click listener
        holder.bind(member, onItemClick)
    }

    override fun getItemCount() = castAndCrew.size
}