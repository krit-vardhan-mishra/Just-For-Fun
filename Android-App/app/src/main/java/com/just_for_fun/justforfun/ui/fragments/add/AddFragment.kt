package com.just_for_fun.justforfun.ui.fragments.add

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.FragmentAddBinding
import com.just_for_fun.justforfun.util.delegates.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel


class AddFragment : Fragment(R.layout.fragment_add) {

    private val binding by viewBinding(FragmentAddBinding::bind)
    private val viewModel: AddViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
