package com.example.homehelperfinder.ui.reports.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.SpendingTrendItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying spending trend data
 */
public class SpendingTrendAdapter extends RecyclerView.Adapter<SpendingTrendAdapter.ViewHolder> {

    private List<SpendingTrendItem> trendItems = new ArrayList<>();

    public void updateData(List<SpendingTrendItem> newItems) {
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
                .inflate(R.layout.item_spending_trend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SpendingTrendItem item = trendItems.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemCount() {
        return trendItems.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPeriod;
        private final TextView tvRevenue;
        private final TextView tvTransactionCount;
        private final TextView tvGrowthRate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPeriod = itemView.findViewById(R.id.tvPeriod);
            tvRevenue = itemView.findViewById(R.id.tvRevenue);
            tvTransactionCount = itemView.findViewById(R.id.tvTransactionCount);
            tvGrowthRate = itemView.findViewById(R.id.tvGrowthRate);
        }

        public void bind(SpendingTrendItem item) {
            // Set period display
            tvPeriod.setText(item.getPeriodDisplay());
            
            // Set revenue
            tvRevenue.setText(item.getFormattedRevenue());
            
            // Set transaction count
            tvTransactionCount.setText(String.valueOf(item.getTransactionCount()));
            
            // Set growth rate with color
            tvGrowthRate.setText(item.getFormattedGrowthRate());
            
            // Set colors based on values
            if (item.hasRevenue()) {
                tvRevenue.setTextColor(itemView.getContext().getColor(R.color.primary));
            } else {
                tvRevenue.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            if (item.hasTransactions()) {
                tvTransactionCount.setTextColor(itemView.getContext().getColor(R.color.green));
            } else {
                tvTransactionCount.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }

            if (item.hasPositiveGrowth()) {
                tvGrowthRate.setTextColor(itemView.getContext().getColor(R.color.green));
            } else if (item.getGrowthRate() < 0) {
                tvGrowthRate.setTextColor(itemView.getContext().getColor(R.color.red));
            } else {
                tvGrowthRate.setTextColor(itemView.getContext().getColor(R.color.text_secondary));
            }
        }
    }
}
