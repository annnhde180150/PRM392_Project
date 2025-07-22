package com.example.homehelperfinder.ui.reports.helper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ServiceBreakdown;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying service breakdown data in RecyclerView
 */
public class ServiceBreakdownAdapter extends RecyclerView.Adapter<ServiceBreakdownAdapter.ServiceBreakdownViewHolder> {
    
    private List<ServiceBreakdown> serviceBreakdowns;

    public ServiceBreakdownAdapter() {
        this.serviceBreakdowns = new ArrayList<>();
    }

    public void updateData(List<ServiceBreakdown> newData) {
        this.serviceBreakdowns.clear();
        if (newData != null) {
            this.serviceBreakdowns.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ServiceBreakdownViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_service_breakdown, parent, false);
        return new ServiceBreakdownViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServiceBreakdownViewHolder holder, int position) {
        ServiceBreakdown service = serviceBreakdowns.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceBreakdowns.size();
    }

    static class ServiceBreakdownViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvServiceName;
        private final TextView tvBookingsCount;
        private final TextView tvTotalEarnings;
        private final TextView tvAverageRating;
        private final TextView tvCompletionRate;
        private final TextView tvAveragePerBooking;

        public ServiceBreakdownViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);
            tvBookingsCount = itemView.findViewById(R.id.tv_bookings_count);
            tvTotalEarnings = itemView.findViewById(R.id.tv_total_earnings);
            tvAverageRating = itemView.findViewById(R.id.tv_average_rating);
            tvCompletionRate = itemView.findViewById(R.id.tv_completion_rate);
            tvAveragePerBooking = itemView.findViewById(R.id.tv_average_per_booking);
        }

        public void bind(ServiceBreakdown service) {
            tvServiceName.setText(service.getServiceDisplayName());
            tvBookingsCount.setText(service.getFormattedBookingsCount() + " đặt chỗ");
            tvTotalEarnings.setText(service.getFormattedTotalEarnings());
            tvAverageRating.setText(service.getFormattedAverageRating() + " ⭐");
            tvCompletionRate.setText(service.getFormattedCompletionRate());
            tvAveragePerBooking.setText(service.getFormattedAverageEarningsPerBooking() + "/đặt chỗ");
            
            // Set different colors based on performance
            if (service.hasEarnings()) {
                tvTotalEarnings.setTextColor(itemView.getContext().getColor(R.color.success));
            } else {
                tvTotalEarnings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }
            
            if (service.hasRating()) {
                tvAverageRating.setTextColor(itemView.getContext().getColor(R.color.warning));
            } else {
                tvAverageRating.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }
        }
    }
}
