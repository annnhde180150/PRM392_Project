package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.RequestItem;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for admin requests API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequestsResponse {
    @SerializedName("requests")
    private List<RequestItem> requests;
    
    @SerializedName("total")
    private int total;
    
    @SerializedName("page")
    private int page;
    
    @SerializedName("pageSize")
    private int pageSize;
    
    /**
     * Check if there are more pages available
     */
    public boolean hasNextPage() {
        return (page * pageSize) < total;
    }
    
    /**
     * Check if this is the first page
     */
    public boolean isFirstPage() {
        return page <= 1;
    }
    
    /**
     * Get total number of pages
     */
    public int getTotalPages() {
        return (int) Math.ceil((double) total / pageSize);
    }
}
