package com.example.homehelperfinder.ui.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ServicePerformanceResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying service performance data in RecyclerView
 */
public class ServicePerformanceAdapter extends RecyclerView.Adapter<ServicePerformanceAdapter.ServicePerformanceViewHolder> {

    private List<ServicePerformanceResponse> serviceList;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public ServicePerformanceAdapter(List<ServicePerformanceResponse> serviceList) {
        this.serviceList = serviceList;
    }

    @NonNull
    @Override
    public ServicePerformanceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_performance, parent, false);
        return new ServicePerformanceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ServicePerformanceViewHolder holder, int position) {
        ServicePerformanceResponse service = serviceList.get(position);
        holder.bind(service);
    }

    @Override
    public int getItemCount() {
        return serviceList.size();
    }

    /**
     * Update the service list with new data
     */
    public void updateServices(List<ServicePerformanceResponse> newServices) {
        serviceList.clear();
        serviceList.addAll(newServices);
        notifyDataSetChanged();
    }

    /**
     * Clear all services
     */
    public void clearServices() {
        serviceList.clear();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for service performance items
     */
    class ServicePerformanceViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvServiceName;
        private final TextView tvBookingsCount;
        private final TextView tvTotalRevenue;
        private final TextView tvAverageRating;
        private final TextView tvMarketShare;

        public ServicePerformanceViewHolder(@NonNull View itemView) {
            super(itemView);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);
            tvBookingsCount = itemView.findViewById(R.id.tv_bookings_count);
            tvTotalRevenue = itemView.findViewById(R.id.tv_total_revenue);
            tvAverageRating = itemView.findViewById(R.id.tv_average_rating);
            tvMarketShare = itemView.findViewById(R.id.tv_market_share);
        }

        public void bind(ServicePerformanceResponse service) {
            // Service name
            tvServiceName.setText(service.getServiceName());
            
            // Bookings count
            tvBookingsCount.setText(service.getFormattedBookingsCount());
            
            // Total revenue
            tvTotalRevenue.setText(service.getFormattedRevenue());
            
            // Average rating
            tvAverageRating.setText(service.getFormattedRating());
            
            // Market share
            tvMarketShare.setText(service.getFormattedMarketShare());
        }
    }
}
