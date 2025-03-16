package com.just_for_fun.justforfun.ui.fragments.celebrity.awards

import android.os.Bundle
import android.view.View
import android.widget.BaseExpandableListAdapter
import android.widget.ExpandableListAdapter
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapters.ExpandableAwardsAdapter
import com.just_for_fun.justforfun.data.Awards
import com.just_for_fun.justforfun.databinding.FragmentCelebrityAwardsBinding
import com.just_for_fun.justforfun.util.delegates.viewBinding

class CelebrityAwardsFragment : Fragment(R.layout.fragment_celebrity_awards) {

    private val binding by viewBinding(FragmentCelebrityAwardsBinding::bind)
    private val viewModel: CelebrityAwardsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.awardsList.observe(viewLifecycleOwner) { awards ->
            awards?.let {
                if (it.isNotEmpty()) {
                    setupExpandableListView(it)
                } else {
                    val builder = AlertDialog.Builder(requireContext())

                    builder.setTitle("No Awards.")
                    builder.setMessage("No awards found for this celebrity.")

                    builder.setPositiveButton("OK") { dialog, _ ->
                        dialog.dismiss()
                    }

                    val dialog = builder.create()
                    dialog.show()
                }
            }
        }
    }

    private fun setupExpandableListView(awards: List<Awards>) {
        val groupedAwards = awards.groupBy { it.name }
        val adapter = ExpandableAwardsAdapter(requireContext(), groupedAwards)
        binding.celebrityAwardsExpandableList.setAdapter(adapter)

        binding.celebrityAwardsExpandableList.setOnGroupExpandListener { groupPosition ->
            val adapter = binding.celebrityAwardsExpandableList.expandableListAdapter as BaseExpandableListAdapter
            val groupView = adapter.getGroupView(groupPosition, true, null, binding.celebrityAwardsExpandableList)
            groupView.findViewById<ImageView>(R.id.arrowIcon)?.animate()?.rotation(180f)?.setDuration(200)?.start()
        }

        binding.celebrityAwardsExpandableList.setOnGroupCollapseListener { groupPosition ->
            val adapter = binding.celebrityAwardsExpandableList.expandableListAdapter as ExpandableListAdapter
            val groupView = adapter.getGroupView(groupPosition, false, null, binding.celebrityAwardsExpandableList)
            groupView.findViewById<ImageView>(R.id.arrowIcon)?.animate()?.rotation(0f)?.setDuration(200)?.start()
        }
    }
}