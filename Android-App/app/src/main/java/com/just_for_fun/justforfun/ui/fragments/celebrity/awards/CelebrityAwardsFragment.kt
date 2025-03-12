package com.just_for_fun.justforfun.ui.fragments.celebrity.awards

import android.os.Bundle
import android.view.View
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.ExpandableAwardsAdapter
import com.just_for_fun.justforfun.data.Awards

class CelebrityAwardsFragment : Fragment(R.layout.fragment_celebrity_awards) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        val groupedAwards: Map<String, List<Awards>> = awardsList.groupBy { it.category }

        val expandableListView = view.findViewById<ExpandableListView>(R.id.celebrity_awards_expandable_list)
        val adapter = ExpandableAwardsAdapter(requireContext(), groupedAwards)
        expandableListView.setAdapter(adapter)
    }
}
