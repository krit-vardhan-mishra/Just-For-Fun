package com.just_for_fun.justforfun.ui.fragments.account

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.FragmentAccountBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private var _binding: FragmentAccountBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAccountBinding.bind(view)

        binding.lifecycleOwner = viewLifecycleOwner

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }

        view.findViewById<ImageButton>(R.id.settings_button).setOnClickListener {
            findNavController().navigate(R.id.settingsFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
