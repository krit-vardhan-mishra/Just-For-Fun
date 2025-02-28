package com.just_for_fun.justforfun.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.databinding.FragmentAccountNotificationCommentedBinding
import com.just_for_fun.justforfun.databinding.FragmentAccountNotificationFollowedBinding
import com.just_for_fun.justforfun.databinding.FragmentAccountNotificationLikedBinding
import com.just_for_fun.justforfun.items.NotificationType

class NotificationAdapter(private val notifications: List<NotificationType>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_COMMENTED = 0
        private const val TYPE_FOLLOWED = 1
        private const val TYPE_LIKED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (notifications[position]) {
            NotificationType.COMMENTED -> TYPE_COMMENTED
            NotificationType.FOLLOWED -> TYPE_FOLLOWED
            NotificationType.LIKED -> TYPE_LIKED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_COMMENTED -> {
                val binding = FragmentAccountNotificationCommentedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                CommentedViewHolder(binding)
            }
            TYPE_FOLLOWED -> {
                val binding = FragmentAccountNotificationFollowedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                FollowedViewHolder(binding)
            }
            else -> {
                val binding = FragmentAccountNotificationLikedBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                LikedViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) { }

    override fun getItemCount(): Int = notifications.size

    class CommentedViewHolder(binding: FragmentAccountNotificationCommentedBinding) :
        RecyclerView.ViewHolder(binding.root)

    class FollowedViewHolder(binding: FragmentAccountNotificationFollowedBinding) :
        RecyclerView.ViewHolder(binding.root)

    class LikedViewHolder(binding: FragmentAccountNotificationLikedBinding) :
        RecyclerView.ViewHolder(binding.root)
}
