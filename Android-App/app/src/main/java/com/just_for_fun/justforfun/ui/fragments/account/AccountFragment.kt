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

        viewModel.currentUser.observe(viewLifecycleOwner) { user ->
            user?.let {
                // Update UI elements with user data
                binding.userName.text = it.username.uppercase()
                binding.userId.text = "@${it.username}"
                binding.userBio.text = "Welcome, ${it.name}!"

                // Set profile photo if available
                if (it.profilePhoto != null) {
                    binding.accountPhoto.background = null
                    // You might want to use Glide or Picasso to load the image
                    // binding.accountPhoto.setImageURI(it.profilePhoto)
                }
            } ?: run {
                // Handle case when no user is logged in
                binding.userName.text = "GUEST"
                binding.userId.text = "@guest"
                binding.userBio.text = "Please log in"
            }
        }

        view.findViewById<ImageButton>(R.id.settings_button).setOnClickListener {
            findNavController().navigate(R.id.nav_settingsFragment)
        }
    }
}
