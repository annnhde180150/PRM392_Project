package com.example.homehelperfinder.ui.viewPendingBooking.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.homehelperfinder.R
import com.example.homehelperfinder.data.model.response.BookingDetailResponse
import com.example.homehelperfinder.utils.DateUtils

class PendingBookingAdapter(
    private var bookingList: List<BookingDetailResponse>,
    private val listener: OnPendingBookingActionListener
) : RecyclerView.Adapter<PendingBookingAdapter.PendingBookingViewHolder>() {

    interface OnPendingBookingActionListener {
        fun onEditBooking(booking: BookingDetailResponse)
        fun onCancelBooking(booking: BookingDetailResponse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingBookingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_booking, parent, false)
        return PendingBookingViewHolder(view)
    }

    override fun onBindViewHolder(holder: PendingBookingViewHolder, position: Int) {
        val booking = bookingList[position]
        holder.bind(booking)
        holder.btnEdit.setOnClickListener {
            listener.onEditBooking(booking)
        }
        holder.btnCancel.setOnClickListener {
            listener.onCancelBooking(booking)
        }
    }

    override fun getItemCount(): Int = bookingList.size

    fun updateBookings(newBookings: List<BookingDetailResponse>) {
        bookingList = newBookings
        notifyDataSetChanged()
    }

    inner class PendingBookingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvScheduledTime: TextView = itemView.findViewById(R.id.tvScheduledTime)
        private val tvEstimatedPrice: TextView = itemView.findViewById(R.id.tvEstimatedPrice)
        private val tvFreeCancellationDeadline: TextView = itemView.findViewById(R.id.tvFreeCancellationDeadline)
        private val tvBookingCreationTime: TextView = itemView.findViewById(R.id.tvBookingCreationTime)
        val btnEdit: Button = itemView.findViewById(R.id.btnEditBooking)
        val btnCancel: Button = itemView.findViewById(R.id.btnCancelBooking)

        fun bind(booking: BookingDetailResponse) {
            tvScheduledTime.text = "Scheduled: ${DateUtils.formatDateTimeForDisplay(booking.scheduledStartTime)} - ${DateUtils.formatDateTimeForDisplay(booking.scheduledEndTime)}"
            tvEstimatedPrice.text = "Estimated Price: ${booking.estimatedPrice?.toString() ?: "N/A"} VND"
            tvFreeCancellationDeadline.text =
                if (!booking.freeCancellationDeadline.isNullOrEmpty())
                    "Free Cancellation Until: ${DateUtils.formatDateTimeForDisplay(booking.freeCancellationDeadline)}"
                else ""
            tvBookingCreationTime.text = "Created: ${DateUtils.getTimeAgo(booking.bookingCreationTime) ?: "N/A"}"
        }
    }
}