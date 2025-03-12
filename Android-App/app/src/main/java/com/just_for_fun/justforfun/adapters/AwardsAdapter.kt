package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Awards

class AwardsAdapter(private val awards: List<Awards>) :
    RecyclerView.Adapter<AwardsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_celebrity_awards, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(awards[position])
    }

    override fun getItemCount() = awards.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(award: Awards) {

            val awardsList = listOf(
                Awards("1", "Best Actor", 2020, "Golden Globe Award for Best Motion Picture – Drama", "For this movie", "Awarded for best performance by an actor"),
                Awards("2", "Best Actress", 2020, "Academy Award for Best Animated Feature Film","For this movie", "Awarded for best performance by an actress"),
                Awards("3", "Lifetime Achievement", 2021, "Prime time Emmy Award for Outstanding Lead Actor in a Drama Series", "For this tv show", "Awarded for outstanding career achievements"),
                Awards("4", "Best Director", 2022, "Grammy Award for Album of the Year","For this movie", "Awarded for best direction in a movie"),
                Awards("5", "Best Supporting Actor", 2020, "Tony Award for Best Play", "For this movie", "Awarded for best supporting actor"),
                Awards("6", "Best Newcomer", 2021, "BAFTA Award for Best British Film", "For best actor in this movie", "Awarded for best debut performance"),
                Awards("7", "Best Comedian", 2022, "Cannes Film Festival Palme d'Or", "For best actor in this movie", "Awarded for best comedic performance"),
                Awards("8", "Best TV Show", 2024, "Critics' Choice Television Award for Best Comedy Series", "For best tv show", "Awarded for best comedic performance")
            )

        }
    }
}

