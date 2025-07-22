package com.example.homehelperfinder.ui.review.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homehelperfinder.R;
import com.example.homehelperfinder.data.model.response.ReviewResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Adapter for displaying user's own reviews in RecyclerView
 */
public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.MyReviewViewHolder> {
    
    private final Context context;
    private List<ReviewResponse> reviews;
    private OnReviewClickListener onReviewClickListener;
    
    public interface OnReviewClickListener {
        void onReviewClick(ReviewResponse review, int position);
    }
    
    public MyReviewAdapter(Context context) {
        this.context = context;
        this.reviews = new ArrayList<>();
    }
    
    public MyReviewAdapter(Context context, List<ReviewResponse> reviews) {
        this.context = context;
        this.reviews = reviews != null ? reviews : new ArrayList<>();
    }
    
    @NonNull
    @Override
    public MyReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_my_review, parent, false);
        return new MyReviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull MyReviewViewHolder holder, int position) {
        ReviewResponse review = reviews.get(position);
        holder.bind(review);
    }
    
    @Override
    public int getItemCount() {
        return reviews.size();
    }
    
    /**
     * Update the reviews list
     */
    public void updateReviews(List<ReviewResponse> newReviews) {
        this.reviews.clear();
        if (newReviews != null) {
            this.reviews.addAll(newReviews);
        }
        notifyDataSetChanged();
    }
    
    /**
     * Add a new review to the list
     */
    public void addReview(ReviewResponse review) {
        if (review != null) {
            reviews.add(0, review); // Add to top
            notifyItemInserted(0);
        }
    }
    
    /**
     * Remove a review from the list
     */
    public void removeReview(int position) {
        if (position >= 0 && position < reviews.size()) {
            reviews.remove(position);
            notifyItemRemoved(position);
        }
    }
    
    /**
     * Get review at position
     */
    public ReviewResponse getReview(int position) {
        if (position >= 0 && position < reviews.size()) {
            return reviews.get(position);
        }
        return null;
    }
    
    /**
     * Set click listener
     */
    public void setOnReviewClickListener(OnReviewClickListener listener) {
        this.onReviewClickListener = listener;
    }
    
    /**
     * Check if list is empty
     */
    public boolean isEmpty() {
        return reviews.isEmpty();
    }
    
    /**
     * ViewHolder class for my review items
     */
    public class MyReviewViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView tvHelperName;
        private final TextView tvServiceName;
        private final TextView tvReviewDate;
        private final RatingBar rbRating;
        private final TextView tvRatingValue;
        private final TextView tvComment;
        private final View itemContainer;
        
        public MyReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvHelperName = itemView.findViewById(R.id.tv_helper_name);
            tvServiceName = itemView.findViewById(R.id.tv_service_name);
            tvReviewDate = itemView.findViewById(R.id.tv_review_date);
            rbRating = itemView.findViewById(R.id.rb_rating);
            tvRatingValue = itemView.findViewById(R.id.tv_rating_value);
            tvComment = itemView.findViewById(R.id.tv_comment);
            itemContainer = itemView.findViewById(R.id.item_container);
            
            // Set click listener
            itemContainer.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && onReviewClickListener != null) {
                    onReviewClickListener.onReviewClick(reviews.get(position), position);
                }
            });
        }
        
        public void bind(ReviewResponse review) {
            // Set helper name
            if (review.getHelperName() != null && !review.getHelperName().isEmpty()) {
                tvHelperName.setText(review.getHelperName());
            } else {
                tvHelperName.setText("Helper");
            }
            
            // Set service name
            if (review.getServiceName() != null && !review.getServiceName().isEmpty()) {
                tvServiceName.setText(review.getServiceName());
                tvServiceName.setVisibility(View.VISIBLE);
            } else {
                tvServiceName.setVisibility(View.GONE);
            }
            
            // Set review date
            tvReviewDate.setText(review.getRelativeTime());
            
            // Set rating
            rbRating.setRating(review.getRatingAsFloat());
            tvRatingValue.setText(review.getFormattedRating());
            
            // Set comment
            if (review.hasComment()) {
                tvComment.setText(review.getComment());
                tvComment.setVisibility(View.VISIBLE);
            } else {
                tvComment.setVisibility(View.GONE);
            }
        }
    }
}
