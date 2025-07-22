package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Response model for Review API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewResponse {
    
    @SerializedName("reviewId")
    private int reviewId;
    
    @SerializedName("bookingId")
    private int bookingId;
    
    @SerializedName("helperId")
    private int helperId;
    
    @SerializedName("userId")
    private int userId;
    
    @SerializedName("rating")
    private int rating;
    
    @SerializedName("comment")
    private String comment;
    
    @SerializedName("reviewDate")
    private String reviewDate;
    
    // Additional fields that might be included in API responses
    @SerializedName("helperName")
    private String helperName;
    
    @SerializedName("userName")
    private String userName;
    
    @SerializedName("userAvatar")
    private String userAvatar;
    
    @SerializedName("serviceName")
    private String serviceName;
    
    /**
     * Get formatted review date
     */
    public String getFormattedDate() {
        if (reviewDate == null || reviewDate.isEmpty()) {
            return "Unknown date";
        }
        
        try {
            // Parse the ISO date format from API
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault());
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
            
            Date date = inputFormat.parse(reviewDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Try alternative format without microseconds
            try {
                SimpleDateFormat inputFormat2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
                SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                
                Date date = inputFormat2.parse(reviewDate);
                return outputFormat.format(date);
            } catch (ParseException e2) {
                return reviewDate; // Return original if parsing fails
            }
        }
    }
    
    /**
     * Get formatted rating text
     */
    public String getFormattedRating() {
        return String.valueOf(rating);
    }
    
    /**
     * Check if rating is valid (1-5 stars)
     */
    public boolean isValidRating() {
        return rating >= 1 && rating <= 5;
    }
    
    /**
     * Get star rating as float for RatingBar
     */
    public float getRatingAsFloat() {
        return (float) rating;
    }
    
    /**
     * Get truncated comment for list display
     */
    public String getTruncatedComment(int maxLength) {
        if (comment == null || comment.isEmpty()) {
            return "No comment provided";
        }
        
        if (comment.length() <= maxLength) {
            return comment;
        }
        
        return comment.substring(0, maxLength) + "...";
    }
    
    /**
     * Get display name for reviewer
     */
    public String getReviewerName() {
        if (userName != null && !userName.isEmpty()) {
            return userName;
        }
        return "Anonymous User";
    }
    
    /**
     * Check if review has comment
     */
    public boolean hasComment() {
        return comment != null && !comment.trim().isEmpty();
    }
    
    /**
     * Get relative time string (e.g., "2 days ago")
     */
    public String getRelativeTime() {
        if (reviewDate == null || reviewDate.isEmpty()) {
            return "Unknown time";
        }
        
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS", Locale.getDefault());
            Date reviewDateTime = inputFormat.parse(reviewDate);
            Date now = new Date();
            
            long diffInMillis = now.getTime() - reviewDateTime.getTime();
            long diffInDays = diffInMillis / (24 * 60 * 60 * 1000);
            
            if (diffInDays == 0) {
                return "Today";
            } else if (diffInDays == 1) {
                return "Yesterday";
            } else if (diffInDays < 7) {
                return diffInDays + " days ago";
            } else if (diffInDays < 30) {
                long weeks = diffInDays / 7;
                return weeks + (weeks == 1 ? " week ago" : " weeks ago");
            } else {
                return getFormattedDate();
            }
        } catch (ParseException e) {
            return getFormattedDate();
        }
    }

    public String getReviewDate(){
        return reviewDate;
    }

    public String getComment(){
        return comment;
    }
}
