package com.example.homehelperfinder.ui.reports.helper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.BookingTrend;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying booking trend data in RecyclerView
 */
public class BookingTrendAdapter extends RecyclerView.Adapter<BookingTrendAdapter.BookingTrendViewHolder> {
    
    private List<BookingTrend> bookingTrends;

    public BookingTrendAdapter() {
        this.bookingTrends = new ArrayList<>();
    }

    public void updateData(List<BookingTrend> newData) {
        this.bookingTrends.clear();
        if (newData != null) {
            this.bookingTrends.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public BookingTrendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_helper_booking_trend, parent, false);
        return new BookingTrendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingTrendViewHolder holder, int position) {
        BookingTrend trend = bookingTrends.get(position);
        holder.bind(trend);
    }

    @Override
    public int getItemCount() {
        return bookingTrends.size();
    }

    static class BookingTrendViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPeriod;
        private final TextView tvTotalBookings;
        private final TextView tvCompletedBookings;
        private final TextView tvCancelledBookings;
        private final TextView tvCompletionRate;

        public BookingTrendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPeriod = itemView.findViewById(R.id.tv_period);
            tvTotalBookings = itemView.findViewById(R.id.tv_total_bookings);
            tvCompletedBookings = itemView.findViewById(R.id.tv_completed_bookings);
            tvCancelledBookings = itemView.findViewById(R.id.tv_cancelled_bookings);
            tvCompletionRate = itemView.findViewById(R.id.tv_completion_rate);
        }

        public void bind(BookingTrend trend) {
            tvPeriod.setText(trend.getFormattedPeriod());
            tvTotalBookings.setText(trend.getFormattedTotalBookings());
            tvCompletedBookings.setText(trend.getFormattedCompletedBookings());
            tvCancelledBookings.setText(trend.getFormattedCancelledBookings());
            tvCompletionRate.setText(trend.getFormattedCompletionRate());
            
            // Set different colors based on completion rate
            double completionRate = trend.getCompletionRate();
            if (completionRate >= 80) {
                tvCompletionRate.setTextColor(itemView.getContext().getColor(R.color.success));
            } else if (completionRate >= 60) {
                tvCompletionRate.setTextColor(itemView.getContext().getColor(R.color.warning));
            } else {
                tvCompletionRate.setTextColor(itemView.getContext().getColor(R.color.error));
            }
            
            // Set colors for bookings
            if (trend.hasBookings()) {
                tvTotalBookings.setTextColor(itemView.getContext().getColor(R.color.primary));
                tvCompletedBookings.setTextColor(itemView.getContext().getColor(R.color.success));
                tvCancelledBookings.setTextColor(itemView.getContext().getColor(R.color.error));
            } else {
                tvTotalBookings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
                tvCompletedBookings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
                tvCancelledBookings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }
        }
    }
}
