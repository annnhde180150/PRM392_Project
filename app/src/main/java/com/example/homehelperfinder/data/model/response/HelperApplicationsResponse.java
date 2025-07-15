package com.example.homehelperfinder.data.model.response;

import com.example.homehelperfinder.data.model.HelperApplication;
import com.example.homehelperfinder.data.model.Pagination;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for helper applications list API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HelperApplicationsResponse {
    @SerializedName("applications")
    private List<HelperApplication> applications;
    
    @SerializedName("pagination")
    private Pagination pagination;
    
    /**
     * Check if there are any applications
     */
    public boolean hasApplications() {
        return applications != null && !applications.isEmpty();
    }
    
    /**
     * Get the number of applications in this response
     */
    public int getApplicationCount() {
        return applications != null ? applications.size() : 0;
    }
    
    /**
     * Check if there are more pages available
     */
    public boolean hasNextPage() {
        return pagination != null && pagination.isHasNext();
    }
    
    /**
     * Check if there are previous pages available
     */
    public boolean hasPreviousPage() {
        return pagination != null && pagination.isHasPrevious();
    }
    
    /**
     * Get current page number
     */
    public int getCurrentPage() {
        return pagination != null ? pagination.getPage() : 1;
    }
    
    /**
     * Get total pages
     */
    public int getTotalPages() {
        return pagination != null ? pagination.getTotalPages() : 1;
    }
    
    /**
     * Get total count of all applications
     */
    public int getTotalCount() {
        return pagination != null ? pagination.getTotalCount() : 0;
    }
}
