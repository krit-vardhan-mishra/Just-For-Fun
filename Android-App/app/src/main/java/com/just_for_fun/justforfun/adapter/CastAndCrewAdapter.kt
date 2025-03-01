package com.just_for_fun.justforfun.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.CastCrewMember

class CastAndCrewAdapter(private val casAndCrew: List<CastCrewMember>) : RecyclerView.Adapter<CastAndCrewAdapter.CastCrewViewHolder>() {

    inner class CastCrewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.movie_activity_cast_and_crew)
        val name: TextView = itemView.findViewById(R.id.cast_crew_name)
        val role: TextView = itemView.findViewById(R.id.cast_crew_role)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastCrewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.cast_and_crew_layout, parent, false)
        return CastCrewViewHolder(view)
    }

    override fun onBindViewHolder(holder: CastCrewViewHolder, position: Int) {
        val member = casAndCrew[position]
        holder.image.setImageResource(member.image)
        holder.name.text = member.name
        holder.role.text = member.role
    }

    override fun getItemCount() = casAndCrew.size
}