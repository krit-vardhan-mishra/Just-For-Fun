package com.just_for_fun.justforfun.ui.fragments.account

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.FragmentAccountBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private lateinit var binding: FragmentAccountBinding
    private lateinit var viewModel: AccountViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentAccountBinding.bind(view)

        viewModel = ViewModelProvider(this)[AccountViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.nav_settingsFragment)
        }

        view.findViewById<ImageButton>(R.id.settings_button).setOnClickListener {
            findNavController().navigate(R.id.nav_settingsFragment)
        }
    }
}
