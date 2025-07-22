package com.example.homehelperfinder.ui.viewFeedBack.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.response.BookingServiceNameResponse
import com.example.homehelperfinder.data.model.response.ReviewResponse
import com.example.homehelperfinder.data.model.response.ServiceResponse
import com.example.homehelperfinder.utils.DateUtils

class FeedBackAdapter(
    private var reviews: List<ReviewResponse>,
    private var services: List<BookingServiceNameResponse>
) : RecyclerView.Adapter<FeedBackAdapter.FeedBackViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedBackViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_feedback, parent, false)
        return FeedBackViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedBackViewHolder, position: Int) {
        val serviceName = services.find { it.bookingId == reviews[position].getBookingId()}?.serviceName
        holder.bind(reviews[position], serviceName.toString())
    }

    override fun getItemCount(): Int = reviews.size

    fun updateReviews(newReviews: List<ReviewResponse>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    inner class FeedBackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvServiceName: TextView = itemView.findViewById(R.id.tvServiceName)
        private val rbFeedback: RatingBar = itemView.findViewById(R.id.rbFeedback)
        private val tvDate: TextView = itemView.findViewById(R.id.tvDate)
        private val tvFeedback: TextView = itemView.findViewById(R.id.tvFeedback)

        fun bind(review: ReviewResponse, serviceName : String) {
            tvServiceName.text = serviceName
            rbFeedback.rating = review.ratingAsFloat
            tvDate.text = DateUtils.getTimeAgo(review.getReviewDate())
            tvFeedback.text = review.getComment()
        }
    }
}