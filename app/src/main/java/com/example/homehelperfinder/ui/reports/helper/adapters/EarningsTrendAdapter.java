package com.example.homehelperfinder.ui.reports.helper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.EarningsTrend;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying earnings trend data in RecyclerView
 */
public class EarningsTrendAdapter extends RecyclerView.Adapter<EarningsTrendAdapter.EarningsTrendViewHolder> {
    
    private List<EarningsTrend> earningsTrends;

    public EarningsTrendAdapter() {
        this.earningsTrends = new ArrayList<>();
    }

    public void updateData(List<EarningsTrend> newData) {
        this.earningsTrends.clear();
        if (newData != null) {
            this.earningsTrends.addAll(newData);
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EarningsTrendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_earnings_trend, parent, false);
        return new EarningsTrendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EarningsTrendViewHolder holder, int position) {
        EarningsTrend trend = earningsTrends.get(position);
        holder.bind(trend);
    }

    @Override
    public int getItemCount() {
        return earningsTrends.size();
    }

    static class EarningsTrendViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPeriod;
        private final TextView tvEarnings;
        private final TextView tvBookingsCount;
        private final TextView tvAveragePerBooking;

        public EarningsTrendViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPeriod = itemView.findViewById(R.id.tv_period);
            tvEarnings = itemView.findViewById(R.id.tv_earnings);
            tvBookingsCount = itemView.findViewById(R.id.tv_bookings_count);
            tvAveragePerBooking = itemView.findViewById(R.id.tv_average_per_booking);
        }

        public void bind(EarningsTrend trend) {
            tvPeriod.setText(trend.getFormattedPeriod());
            tvEarnings.setText(trend.getFormattedEarnings());
            tvBookingsCount.setText(trend.getFormattedBookingsCount() + " đặt chỗ");
            tvAveragePerBooking.setText(trend.getFormattedAverageEarningsPerBooking() + "/đặt chỗ");
            
            // Set different background or text color based on earnings
            if (trend.hasEarnings()) {
                tvEarnings.setTextColor(itemView.getContext().getColor(R.color.success));
            } else {
                tvEarnings.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }
        }
    }
}
