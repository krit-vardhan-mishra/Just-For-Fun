package com.just_for_fun.justforfun.ui.fragments.add

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.FragmentAddBinding

class AddFragment : Fragment(R.layout.fragment_add) {

    private lateinit var binding: FragmentAddBinding
    private val viewModel: AddViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAddBinding.bind(view)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.fragmentAddSearchType.setOnClickListener {
            showPopupMenu()
        }
    }

    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.fragmentAddSearchType)
        popupMenu.menuInflater.inflate(R.menu.search_nav_bar, popupMenu.menu)

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_searchMovie -> {
                    binding.fragmentAddSearchType.text = "MOVIE"
                    true
                }
                R.id.nav_searchTVShow -> {
                    binding.fragmentAddSearchType.text = "TV SHOW"
                    true
                }
                else -> {
                    binding.fragmentAddSearchType.text = "ALL"
                    true
                }
            }
        }

        popupMenu.show()
    }
}
