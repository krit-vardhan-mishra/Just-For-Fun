package com.just_for_fun.justforfun.ui.fragments.account

import android.os.Bundle
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.View
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.FragmentAccountBinding
import com.just_for_fun.justforfun.util.delegates.viewBinding

class AccountFragment : Fragment(R.layout.fragment_account) {

    private val binding by viewBinding(FragmentAccountBinding::bind)
    private val viewModel: AccountViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
