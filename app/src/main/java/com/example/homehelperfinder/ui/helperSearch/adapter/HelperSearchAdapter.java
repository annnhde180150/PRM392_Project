package com.example.homehelperfinder.ui.helperSearch.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.HelperSearchResponse;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HelperSearchAdapter extends RecyclerView.Adapter<HelperSearchAdapter.HelperViewHolder> {
    private List<HelperSearchResponse> helperList;
    private OnHelperClickListener listener;

    public interface OnHelperClickListener {
        void onHelperSelected(HelperSearchResponse helper);
        void onHelperContact(HelperSearchResponse helper);
        void onAddFavorite(HelperSearchResponse helper);
    }

    public HelperSearchAdapter(List<HelperSearchResponse> helperList, OnHelperClickListener listener) {
        this.helperList = helperList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HelperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_helper_search, parent, false);
        return new HelperViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HelperViewHolder holder, int position) {
        HelperSearchResponse helper = helperList.get(position);
        holder.bind(helper);
    }

    @Override
    public int getItemCount() {
        return helperList.size();
    }

    class HelperViewHolder extends RecyclerView.ViewHolder {
        private View rootView;
        private ImageView ivProfilePicture;
        private TextView tvHelperName;
        private TextView tvServiceName;
        private TextView tvBio;
        private TextView tvPrice;
        private TextView tvRating;
        private TextView tvWorkArea;
        private MaterialButton btnContact;
        private View availabilityIndicator;
        private TextView tvBStatus;
        private MaterialButton btnAddFavorite;

        public HelperViewHolder(@NonNull View itemView) {
            super(itemView);
            rootView = itemView;
            ivProfilePicture = itemView.findViewById(R.id.ivProfilePicture);
            tvHelperName = itemView.findViewById(R.id.tvHelperName);
            tvServiceName = itemView.findViewById(R.id.tvServiceName);
            tvBio = itemView.findViewById(R.id.tvBio);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvWorkArea = itemView.findViewById(R.id.tvWorkArea);
            btnContact = itemView.findViewById(R.id.btnContact);
            availabilityIndicator = itemView.findViewById(R.id.availabilityIndicator);
            tvBStatus = itemView.findViewById(R.id.tvBStatus);
            btnAddFavorite = itemView.findViewById(R.id.btnAddFavorite);
            rootView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onHelperSelected(helperList.get(position));
                }
            });

            btnContact.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onHelperContact(helperList.get(position));
                }
            });

            btnAddFavorite.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && listener != null) {
                    listener.onAddFavorite(helperList.get(position));
                }
            });
        }

        public void bind(HelperSearchResponse helper) {
            tvHelperName.setText(helper.getHelperName());
            tvServiceName.setText(helper.getServiceName());
            tvPrice.setText(helper.getFormattedPrice());
            tvRating.setText("Rating:" + helper.getFormattedRating());
            tvWorkArea.setText(helper.getFormattedWorkArea());
            tvBio.setText(helper.getBio());

            Context context = itemView.getContext();
            switch (helper.getAvailableStatus() != null ? helper.getAvailableStatus() : "Offline") {
                case "Available":
                    availabilityIndicator.setBackgroundResource(R.drawable.bg_status_online);
                    tvBStatus.setText("Status: " + helper.getAvailableStatus());
                    break;
                case "Busy":
                    availabilityIndicator.setBackgroundResource(R.drawable.bg_status_busy);
                    tvBStatus.setText("Status: " + helper.getAvailableStatus());
                    break;
                default:
                    availabilityIndicator.setBackgroundResource(R.drawable.bg_status_offline);
                    tvBStatus.setText("Status: " + helper.getAvailableStatus());
                    break;
            }
            // Ảnh đại diện: dùng ảnh mặc định hoặc có thể bổ sung sau nếu API trả về
            ivProfilePicture.setImageResource(R.drawable.illustration_home_electronic_fixing);
        }
    }
} 