package com.example.homehelperfinder.ui.reports.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.FavoriteHelperReportResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying favorite helpers report data
 */
public class FavoriteHelpersReportAdapter extends RecyclerView.Adapter<FavoriteHelpersReportAdapter.ViewHolder> {

    private List<FavoriteHelperReportResponse> helpers = new ArrayList<>();

    public void updateData(List<FavoriteHelperReportResponse> newHelpers) {
        this.helpers.clear();
        if (newHelpers != null) {
            this.helpers.addAll(newHelpers);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_favorite_helper_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FavoriteHelperReportResponse helper = helpers.get(position);
        holder.bind(helper);
    }

    @Override
    public int getItemCount() {
        return helpers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvHelperName;
        private final TextView tvEmail;
        private final TextView tvTotalBookings;
        private final TextView tvCompletedBookings;
        private final TextView tvCompletionRate;
        private final TextView tvAverageRating;
        private final TextView tvTotalEarnings;
        private final TextView tvAverageBookingValue;
        private final TextView tvTotalHoursWorked;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHelperName = itemView.findViewById(R.id.tvHelperName);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvTotalBookings = itemView.findViewById(R.id.tvTotalBookings);
            tvCompletedBookings = itemView.findViewById(R.id.tvCompletedBookings);
            tvCompletionRate = itemView.findViewById(R.id.tvCompletionRate);
            tvAverageRating = itemView.findViewById(R.id.tvAverageRating);
            tvTotalEarnings = itemView.findViewById(R.id.tvTotalEarnings);
            tvAverageBookingValue = itemView.findViewById(R.id.tvAverageBookingValue);
            tvTotalHoursWorked = itemView.findViewById(R.id.tvTotalHoursWorked);
        }

        public void bind(FavoriteHelperReportResponse helper) {
            // Basic info
            tvHelperName.setText(helper.getHelperName());
            tvEmail.setText(helper.getEmail());
            
            // Booking statistics
            tvTotalBookings.setText(String.valueOf(helper.getTotalBookings()));
            tvCompletedBookings.setText(String.valueOf(helper.getCompletedBookings()));
            tvCompletionRate.setText(helper.getFormattedCompletionRate());
            
            // Rating
            tvAverageRating.setText(helper.getFormattedAverageRating());
            
            // Financial data
            tvTotalEarnings.setText(helper.getFormattedTotalEarnings());
            tvAverageBookingValue.setText(helper.getFormattedAverageBookingValue());
            
            // Work hours
            tvTotalHoursWorked.setText(helper.getFormattedTotalHoursWorked());

            // Set colors based on values
            if (helper.hasBookings()) {
                tvTotalBookings.setTextColor(itemView.getContext().getColor(R.color.primary));
                tvCompletedBookings.setTextColor(itemView.getContext().getColor(R.color.green));
            } else {
                tvTotalBookings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
                tvCompletedBookings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            if (helper.hasReviews()) {
                tvAverageRating.setTextColor(itemView.getContext().getColor(R.color.orange));
            } else {
                tvAverageRating.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            if (helper.hasEarnings()) {
                tvTotalEarnings.setTextColor(itemView.getContext().getColor(R.color.primary));
                tvAverageBookingValue.setTextColor(itemView.getContext().getColor(R.color.primary));
            } else {
                tvTotalEarnings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
                tvAverageBookingValue.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            // Set completion rate color
            if (helper.getCompletionRate() > 0.8) {
                tvCompletionRate.setTextColor(itemView.getContext().getColor(R.color.green));
            } else if (helper.getCompletionRate() > 0.5) {
                tvCompletionRate.setTextColor(itemView.getContext().getColor(R.color.orange));
            } else {
                tvCompletionRate.setTextColor(itemView.getContext().getColor(R.color.red));
            }
        }
    }
}
