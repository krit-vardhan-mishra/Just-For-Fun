package com.just_for_fun.justforfun.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.databinding.FragmentAccountNotificationCommentedBinding
import com.just_for_fun.justforfun.databinding.FragmentAccountNotificationFollowedBinding
import com.just_for_fun.justforfun.databinding.FragmentAccountNotificationLikedBinding
import com.just_for_fun.justforfun.items.NotificationType
import com.just_for_fun.justforfun.util.SimpleDiffCallback

class NotificationAdapter :
    ListAdapter<NotificationType, RecyclerView.ViewHolder>(
        SimpleDiffCallback(
            areItemsSame = { old, new -> old == new },
            areContentsSame = { old, new -> old == new }
        )
    ) {

    companion object {
        private const val TYPE_COMMENTED = 0
        private const val TYPE_FOLLOWED = 1
        private const val TYPE_LIKED = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            NotificationType.COMMENTED -> TYPE_COMMENTED
            NotificationType.FOLLOWED -> TYPE_FOLLOWED
            NotificationType.LIKED -> TYPE_LIKED
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            TYPE_COMMENTED -> CommentedViewHolder(
                FragmentAccountNotificationCommentedBinding.inflate(inflater, parent, false)
            )
            TYPE_FOLLOWED -> FollowedViewHolder(
                FragmentAccountNotificationFollowedBinding.inflate(inflater, parent, false)
            )
            else -> LikedViewHolder(
                FragmentAccountNotificationLikedBinding.inflate(inflater, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        // You can add specific data binding logic here in the future
    }

    class CommentedViewHolder(binding: FragmentAccountNotificationCommentedBinding) :
        RecyclerView.ViewHolder(binding.root)

    class FollowedViewHolder(binding: FragmentAccountNotificationFollowedBinding) :
        RecyclerView.ViewHolder(binding.root)

    class LikedViewHolder(binding: FragmentAccountNotificationLikedBinding) :
        RecyclerView.ViewHolder(binding.root)
}
