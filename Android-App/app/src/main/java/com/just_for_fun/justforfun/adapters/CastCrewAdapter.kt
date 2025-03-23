package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.CastCrewMember
import com.just_for_fun.justforfun.util.SimpleDiffCallback

class CastCrewAdapter(
    private val onItemClick: (CastCrewMember) -> Unit
) : ListAdapter<CastCrewMember, CastCrewAdapter.CastCrewViewHolder>(diffCallback) {

    inner class CastCrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.cast_crew_image)
        val name: TextView = itemView.findViewById(R.id.cast_crew_name)
        val role: TextView = itemView.findViewById(R.id.cast_crew_role)

        fun bind(member: CastCrewMember) {
            itemView.setOnClickListener { onItemClick(member) }
        }
    }

    companion object {
        private val diffCallback = SimpleDiffCallback<CastCrewMember>(
            areItemsSame = { old, new -> old.id == new.id },
            areContentsSame = { old, new -> old == new }
        )
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastCrewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cast_and_crew_layout, parent, false)
        return CastCrewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastCrewViewHolder, position: Int) {
        val member = getItem(position)
        Glide.with(holder.itemView.context)
            .load(member.image)
            .placeholder(R.drawable.placeholder_image)
            .into(holder.image)
        holder.name.text = member.name
        holder.role.text = member.role
        holder.bind(member)
    }
}