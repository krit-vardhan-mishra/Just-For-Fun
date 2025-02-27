package com.just_for_fun.justforfun.ui.fragments.notification

import androidx.fragment.app.viewModels
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.databinding.FragmentHomeBinding
import com.just_for_fun.justforfun.databinding.FragmentNotificationBinding
import com.just_for_fun.justforfun.ui.fragments.home.HomeViewModel

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }

}