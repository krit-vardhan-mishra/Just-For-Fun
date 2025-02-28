package com.just_for_fun.justforfun.ui.fragments.notification

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.adapter.NotificationAdapter
import com.just_for_fun.justforfun.databinding.FragmentNotificationBinding
import com.just_for_fun.justforfun.items.NotificationType

class NotificationFragment : Fragment(R.layout.fragment_notification) {

    private lateinit var binding: FragmentNotificationBinding
    private val viewModel: NotificationViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentNotificationBinding.bind(view)

        val notifications = listOf(
            NotificationType.COMMENTED,
            NotificationType.FOLLOWED,
            NotificationType.LIKED,
            NotificationType.COMMENTED,
            NotificationType.LIKED,
            NotificationType.FOLLOWED,
            NotificationType.COMMENTED,
            NotificationType.FOLLOWED,
            NotificationType.LIKED,
            NotificationType.COMMENTED,
            NotificationType.COMMENTED,
            NotificationType.FOLLOWED,
            NotificationType.LIKED,
            NotificationType.COMMENTED,
            NotificationType.FOLLOWED,
            NotificationType.FOLLOWED
        )

        val adapter = NotificationAdapter(notifications)

        binding.notificationsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.notificationsRecyclerView.adapter = adapter

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
    }
}
