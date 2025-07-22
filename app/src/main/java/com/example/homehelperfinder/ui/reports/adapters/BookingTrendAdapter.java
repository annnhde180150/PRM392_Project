package com.example.homehelperfinder.ui.reports.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.BookingTrendItem;
import com.example.homehelperfinder.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying booking trend data
 */
public class BookingTrendAdapter extends RecyclerView.Adapter<BookingTrendAdapter.ViewHolder> {

    private List<BookingTrendItem> trendItems = new ArrayList<>();

    public void updateData(List<BookingTrendItem> newItems) {
        this.trendItems.clear();
        if (newItems != null) {
            this.trendItems.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_booking_trend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookingTrendItem item = trendItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return trendItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvDate;
        private final TextView tvBookingsCount;
        private final TextView tvEarningsAmount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBookingsCount = itemView.findViewById(R.id.tvBookingsCount);
            tvEarningsAmount = itemView.findViewById(R.id.tvEarningsAmount);
        }

        public void bind(BookingTrendItem item) {
            // Format date for display
            String formattedDate = DateUtils.formatDateForDisplay(item.getDate());
            tvDate.setText(formattedDate);
            
            // Set bookings count
            tvBookingsCount.setText(String.valueOf(item.getBookingsCount()));
            
            // Set earnings amount
            tvEarningsAmount.setText(item.getFormattedEarnings());

            // Set text colors based on values
            if (item.hasBookings()) {
                tvBookingsCount.setTextColor(itemView.getContext().getColor(R.color.green));
            } else {
                tvBookingsCount.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            if (item.hasEarnings()) {
                tvEarningsAmount.setTextColor(itemView.getContext().getColor(R.color.primary));
            } else {
                tvEarningsAmount.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }
        }
    }
}
