package com.just_for_fun.justforfun.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.just_for_fun.justforfun.R
import com.just_for_fun.justforfun.data.Review
import com.just_for_fun.justforfun.data.Reply
import me.zhanghai.android.materialratingbar.MaterialRatingBar
import java.util.*

class ReviewsAdapter(private var reviews: List<Review>) :
    RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userAvatar: ImageView = itemView.findViewById(R.id.review_user_avatar)
        val username: TextView = itemView.findViewById(R.id.review_username)
        val date: TextView = itemView.findViewById(R.id.review_date)
        val rating: MaterialRatingBar = itemView.findViewById(R.id.review_rating)
        val comment: TextView = itemView.findViewById(R.id.review_comment)

        // Interaction elements
        val likeContainer: View = itemView.findViewById(R.id.review_like_container)
        val likeIcon: ImageView = itemView.findViewById(R.id.review_like_icon)
        val likeCount: TextView = itemView.findViewById(R.id.review_like_count)
        val replyContainer: View = itemView.findViewById(R.id.review_reply_container)
        val replyCount: TextView = itemView.findViewById(R.id.review_reply_count)
        val reportButton: ImageView = itemView.findViewById(R.id.review_report_button)

        // Reply sections
        val repliesContainer: LinearLayout = itemView.findViewById(R.id.review_replies_container)
        val replyInputContainer: LinearLayout = itemView.findViewById(R.id.review_reply_input_container)
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

        // Set basic review information
        holder.userAvatar.setImageResource(review.avatarResId)
        holder.username.text = review.username
        holder.date.text = formatDate(review.date)
        holder.rating.rating = review.rating
        holder.comment.text = review.comment

        // Update like UI
        updateLikeUI(holder, review)
        holder.likeCount.text = review.likeCount.toString()
        holder.replyCount.text = review.replies.size.toString()

        // Set click listeners
        holder.likeContainer.setOnClickListener { toggleLike(holder, review) }
        holder.replyContainer.setOnClickListener { toggleReplyInput(holder) }
        holder.reportButton.setOnClickListener {
            Toast.makeText(context, "Review reported", Toast.LENGTH_SHORT).show()
        }

        // Handle reply submission
        holder.replySend.setOnClickListener {
            val replyText = holder.replyInput.text.toString().trim()
            if (replyText.isNotEmpty()) {
                submitReply(context, holder, review, replyText)
                holder.replyInput.text.clear()
                holder.replyInputContainer.visibility = View.GONE
            }
        }

        // Configure replies
        setupReplies(context, holder, review)
    }

    private fun toggleLike(holder: ReviewViewHolder, review: Review) {
        review.isLiked = !review.isLiked
        review.likeCount += if (review.isLiked) 1 else -1
        updateLikeUI(holder, review)
        notifyItemChanged(holder.adapterPosition)
    }

    private fun updateLikeUI(holder: ReviewViewHolder, review: Review) {
        val colorRes = if (review.isLiked) R.color.colorPrimary else android.R.color.darker_gray
        holder.likeIcon.setColorFilter(
            ContextCompat.getColor(holder.itemView.context, colorRes)
        )
        holder.likeCount.text = review.likeCount.toString()
    }

    private fun toggleReplyInput(holder: ReviewViewHolder) {
        val isVisible = holder.replyInputContainer.visibility == View.VISIBLE
        holder.replyInputContainer.visibility = if (isVisible) View.GONE else View.VISIBLE
        if (!isVisible) holder.repliesContainer.visibility = View.VISIBLE
    }

    private fun submitReply(context: Context, holder: ReviewViewHolder, review: Review, text: String) {
        val newReply = Reply(
            id = UUID.randomUUID().toString(),
            username = "Current User",
            avatarResId = R.drawable.account_circle,
            comment = text,
            date = Date(),
            likedCount = 0,
            isLiked = false
        )

        review.replies = review.replies.toMutableList().apply { add(newReply) }
        setupReplies(context, holder, review)
        holder.replyCount.text = review.replies.size.toString()
    }

    private fun setupReplies(context: Context, holder: ReviewViewHolder, review: Review) {
        holder.repliesContainer.removeAllViews()

        review.replies.forEach { reply ->
            val replyView = LayoutInflater.from(context)
                .inflate(R.layout.item_reply, holder.repliesContainer, false)

            // Configure reply view elements
            val avatar = replyView.findViewById<ImageView>(R.id.reply_user_avatar)
            val username = replyView.findViewById<TextView>(R.id.reply_username)
            val date = replyView.findViewById<TextView>(R.id.reply_date)
            val comment = replyView.findViewById<TextView>(R.id.reply_comment)
            val likeContainer = replyView.findViewById<View>(R.id.reply_like_container)
            val likeIcon = replyView.findViewById<ImageView>(R.id.reply_like_icon)
            val likeCount = replyView.findViewById<TextView>(R.id.reply_like_count)

            // Populate the reply view
            avatar.setImageResource(reply.avatarResId)
            username.text = reply.username
            date.text = formatDate(reply.date)
            comment.text = reply.comment
            likeCount.text = reply.likedCount.toString()

            // Set like status
            if (reply.isLiked) {
                likeIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
            } else {
                likeIcon.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray))
            }

            // Handle like click for replies
            likeContainer.setOnClickListener {
                reply.isLiked = !reply.isLiked
                reply.likedCount += if (reply.isLiked) 1 else -1
                likeCount.text = reply.likedCount.toString()

                if (reply.isLiked) {
                    likeIcon.setColorFilter(ContextCompat.getColor(context, R.color.colorPrimary))
                } else {
                    likeIcon.setColorFilter(ContextCompat.getColor(context, android.R.color.darker_gray))
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

    private fun formatDate(date: Date): String {
        val diff = (Date().time - date.time) / 1000
        return when {
            diff < 60 -> "Just now"
            diff < 3600 -> "${diff / 60}m ago"
            diff < 86400 -> "${diff / 3600}h ago"
            diff < 2592000 -> "${diff / 86400}d ago"
            else -> "${diff / 2592000}mo ago"
        }
    }

    override fun getItemCount() = reviews.size

    fun updateReviews(newReviews: List<Review>) {
        val diffCallback = ReviewDiffCallback(reviews, newReviews)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.reviews = newReviews
        diffResult.dispatchUpdatesTo(this)
    }

    class ReviewDiffCallback(
        private val oldList: List<Review>,
        private val newList: List<Review>
    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].id == newList[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}