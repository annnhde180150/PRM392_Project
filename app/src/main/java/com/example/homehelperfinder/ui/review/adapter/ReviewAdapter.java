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
 * Adapter for displaying reviews in RecyclerView
 */
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    
    private final Context context;
    private List<ReviewResponse> reviews;
    private OnReviewClickListener onReviewClickListener;
    
    public interface OnReviewClickListener {
        void onReviewClick(ReviewResponse review, int position);
    }
    
    public ReviewAdapter(Context context) {
        this.context = context;
        this.reviews = new ArrayList<>();
    }
    
    public ReviewAdapter(Context context, List<ReviewResponse> reviews) {
        this.context = context;
        this.reviews = reviews != null ? reviews : new ArrayList<>();
    }
    
    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
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
     * ViewHolder class for review items
     */
    public class ReviewViewHolder extends RecyclerView.ViewHolder {
        
        private final TextView tvReviewerName;
        private final TextView tvReviewDate;
        private final RatingBar rbRating;
        private final TextView tvRatingValue;
        private final TextView tvComment;
        private final View itemContainer;
        
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            
            tvReviewerName = itemView.findViewById(R.id.tv_reviewer_name);
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
            // Set reviewer name
            tvReviewerName.setText(review.getReviewerName());
            
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
