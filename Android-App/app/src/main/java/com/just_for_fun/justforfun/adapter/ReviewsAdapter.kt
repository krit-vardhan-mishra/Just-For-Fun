package com.just_for_fun.justforfun.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Reply
import com.just_for_fun.justforfun.data.Review
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.util.Date

class ReviewsAdapter(private var reviews: List<Review>) :
    RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: ImageView = itemView.findViewById(R.id.review_user_avatar)
        val username: TextView = itemView.findViewById(R.id.review_username)
        val date: TextView = itemView.findViewById(R.id.review_date)
        val rating: MaterialRatingBar = itemView.findViewById(R.id.review_rating)
        val comment: TextView = itemView.findViewById(R.id.review_comment)

        // Action buttons and containers
        val likeContainer: View = itemView.findViewById(R.id.review_like_container)
        val likeIcon: ImageView = itemView.findViewById(R.id.review_like_icon)
        val likeCount: TextView = itemView.findViewById(R.id.review_like_count)
        val replyContainer: View = itemView.findViewById(R.id.review_reply_container)
        val replyCount: TextView = itemView.findViewById(R.id.review_reply_count)
        val reportButton: ImageView = itemView.findViewById(R.id.review_report_button)

        // Reply section
        val repliesContainer: LinearLayout = itemView.findViewById(R.id.review_replies_container)
        val replyInputContainer: View = itemView.findViewById(R.id.review_reply_input_container)
        val replyInput: EditText = itemView.findViewById(R.id.review_reply_input)
        val replySend: ImageButton = itemView.findViewById(R.id.review_reply_send)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val context = holder.itemView.context
        val review = reviews[position]

        // Set basic review info
        holder.userAvatar.setImageResource(review.avatarResId)
        holder.username.text = review.username
        holder.date.text = formatDate(review.date)
        holder.rating.rating = review.rating
        holder.comment.text = review.comment

        // Set like status and count
        updateLikeUI(holder, review)

        // Set reply count
        holder.replyCount.text = review.replies.size.toString()

        // Handle like button click
        holder.likeContainer.setOnClickListener {
            toggleLike(holder, review)
        }

        // Handle reply button click
        holder.replyContainer.setOnClickListener {
            toggleReplyInput(holder)
        }

        // Handle report button click
        holder.reportButton.setOnClickListener {
            Toast.makeText(context, "Review reported", Toast.LENGTH_SHORT).show()
        }

        // Handle send reply button
        holder.replySend.setOnClickListener {
            val replyText = holder.replyInput.text.toString().trim()
            if (replyText.isNotEmpty()) {
                addReply(context, holder, review, replyText)
                holder.replyInput.text.clear()
                // Hide the reply input
                holder.replyInputContainer.visibility = View.GONE
            }
        }

        // Set up replies if any exist
        setupReplies(context, holder, review)
    }

    private fun toggleLike(holder: ReviewViewHolder, review: Review) {
        review.isLiked = !review.isLiked
        review.likeCount += if (review.isLiked) 1 else -1
        updateLikeUI(holder, review)
    }

    private fun updateLikeUI(holder: ReviewViewHolder, review: Review) {
        holder.likeCount.text = review.likeCount.toString()
        if (review.isLiked) {
            holder.likeIcon.setImageResource(R.drawable.ic_like)
            holder.likeIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, com.google.android.material.R.color.design_default_color_primary))
        } else {
            holder.likeIcon.setImageResource(R.drawable.ic_like)
            holder.likeIcon.setColorFilter(ContextCompat.getColor(holder.itemView.context, android.R.color.darker_gray))
        }
    }

    private fun toggleReplyInput(holder: ReviewViewHolder) {
        // Toggle reply input visibility
        holder.replyInputContainer.visibility = if (holder.replyInputContainer.visibility == View.VISIBLE) {
            View.GONE
        } else {
            View.VISIBLE
        }

        // Show replies if they exist
        if (holder.repliesContainer.childCount > 0) {
            holder.repliesContainer.visibility = View.VISIBLE
        }
    }

    private fun addReply(context: Context, holder: ReviewViewHolder, review: Review, replyText: String) {
        // Create a new reply
        val newReply = Reply(
            id = "reply_${System.currentTimeMillis()}",
            username = "You",
            avatarResId = R.drawable.account_fade_gradient,
            comment = replyText,
            date = Date(System.currentTimeMillis()) // Convert Long to Date
        )

        review.replies = (review.replies + newReply).toMutableList()

        // Update the UI
        setupReplies(context, holder, review)

        // Update reply count
        holder.replyCount.text = review.replies.size.toString()
    }

    private fun setupReplies(context: Context, holder: ReviewViewHolder, review: Review) {
        // Clear existing replies
        holder.repliesContainer.removeAllViews()

        // Add each reply
        for (reply in review.replies) {
            val replyView = LayoutInflater.from(context)
                .inflate(R.layout.item_review, holder.repliesContainer, false)

            // Set reply data
            val replyAvatar = replyView.findViewById<ImageView>(R.id.review_user_avatar)
            val replyUsername = replyView.findViewById<TextView>(R.id.review_username)
            val replyDate = replyView.findViewById<TextView>(R.id.review_date)
            val replyText = replyView.findViewById<TextView>(R.id.review_comment)
            val replyLikeContainer = replyView.findViewById<View>(R.id.review_like_container)
            val replyLikeIcon = replyView.findViewById<ImageView>(R.id.review_like_icon)
            val replyLikeCount = replyView.findViewById<TextView>(R.id.review_like_count)

            // Populate the reply view
            replyAvatar.setImageResource(reply.avatarResId)
            replyUsername.text = reply.username
            replyDate.text = formatDate(reply.date)
            replyText.text = reply.comment
            replyLikeCount.text = reply.likedCount.toString()

            // Set like status
            if (reply.isLiked) {
                replyLikeIcon.setColorFilter(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_primary))
            } else {
                replyLikeIcon.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray))
            }

            // Handle like click for replies
            replyLikeContainer.setOnClickListener {
                reply.isLiked = !reply.isLiked
                reply.likedCount += if (reply.isLiked) 1 else -1
                replyLikeCount.text = reply.likedCount.toString()

                if (reply.isLiked) {
                    replyLikeIcon.setColorFilter(ContextCompat.getColor(context, com.google.android.material.R.color.design_default_color_primary))
                } else {
                    replyLikeIcon.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray))
                }
            }

            // Add the reply view to the container
            holder.repliesContainer.addView(replyView)
        }

        // Show replies container if there are replies
        if (review.replies.isNotEmpty()) {
            holder.repliesContainer.visibility = View.VISIBLE
        }
    }

    private fun formatDate(timestamp: Date): String {
        // This is a simple implementation, you might want to use DateUtils or a more sophisticated approach
        val now = System.currentTimeMillis()
        val diffInMillis = now - timestamp.time

        return when {
            diffInMillis < 60000 -> "Just now"
            diffInMillis < 3600000 -> "${diffInMillis / 60000} min ago"
            diffInMillis < 86400000 -> "${diffInMillis / 3600000} hours ago"
            diffInMillis < 604800000 -> "${diffInMillis / 86400000} days ago"
            else -> "${diffInMillis / 604800000} weeks ago"
        }
    }

    override fun getItemCount() = reviews.size

    fun updateReviews(newReviews: List<Review>) {
        this.reviews = newReviews
        notifyDataSetChanged()
    }

    // Add a method to add a single review
    fun addReview(review: Review) {
        this.reviews = this.reviews + review
        notifyItemInserted(reviews.size - 1)
    }

    // Add a method to remove a review
    fun removeReview(reviewId: String) {
        val position = reviews.indexOfFirst { it.id == reviewId }
        if (position != -1) {
            this.reviews = this.reviews.filterNot { it.id == reviewId }
            notifyItemRemoved(position)
        }
    }
}