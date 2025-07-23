package com.example.homehelperfinder.ui.admin.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.HelperRankingResponse;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

/**
 * Adapter for displaying helper ranking data in RecyclerView
 */
public class HelperRankingAdapter extends RecyclerView.Adapter<HelperRankingAdapter.HelperRankingViewHolder> {

    private List<HelperRankingResponse> helperList;
    private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);

    public HelperRankingAdapter(List<HelperRankingResponse> helperList) {
        this.helperList = helperList;
    }

    @NonNull
    @Override
    public HelperRankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_helper_ranking, parent, false);
        return new HelperRankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelperRankingViewHolder holder, int position) {
        HelperRankingResponse helper = helperList.get(position);
        holder.bind(helper, position + 1);
    }

    @Override
    public int getItemCount() {
        return helperList.size();
    }

    /**
     * Update the helper list with new data
     */
    public void updateHelpers(List<HelperRankingResponse> newHelpers) {
        helperList.clear();
        helperList.addAll(newHelpers);
        notifyDataSetChanged();
    }

    /**
     * Clear all helpers
     */
    public void clearHelpers() {
        helperList.clear();
        notifyDataSetChanged();
    }

    /**
     * ViewHolder class for helper ranking items
     */
    class HelperRankingViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvRank;
        private final TextView tvHelperName;
        private final TextView tvHelperEmail;
        private final TextView tvTotalBookings;
        private final TextView tvCompletionRate;
        private final TextView tvAverageRating;
        private final TextView tvTotalEarnings;

        public HelperRankingViewHolder(@NonNull View itemView) {
            super(itemView);
            tvRank = itemView.findViewById(R.id.tv_rank);
            tvHelperName = itemView.findViewById(R.id.tv_helper_name);
            tvHelperEmail = itemView.findViewById(R.id.tv_helper_email);
            tvTotalBookings = itemView.findViewById(R.id.tv_total_bookings);
            tvCompletionRate = itemView.findViewById(R.id.tv_completion_rate);
            tvAverageRating = itemView.findViewById(R.id.tv_average_rating);
            tvTotalEarnings = itemView.findViewById(R.id.tv_total_earnings);
        }

        public void bind(HelperRankingResponse helper, int rank) {
            // Rank
            tvRank.setText(String.valueOf(rank));
            
            // Helper name
            tvHelperName.setText(helper.getHelperName());
            
            // Helper email
            tvHelperEmail.setText(helper.getEmail());
            
            // Total bookings
            tvTotalBookings.setText(String.valueOf(helper.getTotalBookings()));
            
            // Completion rate
            tvCompletionRate.setText(helper.getFormattedCompletionRate());
            
            // Average rating
            tvAverageRating.setText(helper.getFormattedRating());
            
            // Total earnings
            tvTotalEarnings.setText(helper.getFormattedEarnings());
            
            // Set rank styling
            setRankStyling(rank);
        }
        
        private void setRankStyling(int rank) {
            // Apply special styling for top 3 helpers
            switch (rank) {
                case 1:
                    tvRank.setBackgroundResource(R.drawable.bg_rank_gold);
                    break;
                case 2:
                    tvRank.setBackgroundResource(R.drawable.bg_rank_silver);
                    break;
                case 3:
                    tvRank.setBackgroundResource(R.drawable.bg_rank_bronze);
                    break;
                default:
                    tvRank.setBackgroundResource(R.drawable.bg_rank_default);
                    break;
            }
        }
    }
}
