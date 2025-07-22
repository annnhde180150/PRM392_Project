package com.example.homehelperfinder.data.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Response model for chat search API
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchChatResponse {
    
    @SerializedName("users")
    private List<UserSearchResult> users;
    
    @SerializedName("helpers")
    private List<HelperSearchResult> helpers;
    
    @SerializedName("totalUsers")
    private Integer totalUsers;
    
    @SerializedName("totalHelpers")
    private Integer totalHelpers;
    
    @SerializedName("currentPage")
    private Integer currentPage;
    
    @SerializedName("pageSize")
    private Integer pageSize;
    
    @SerializedName("totalPages")
    private Integer totalPages;
    
    @SerializedName("hasNextPage")
    private Boolean hasNextPage;
    
    @SerializedName("hasPreviousPage")
    private Boolean hasPreviousPage;
    
    /**
     * Get total number of results (users + helpers)
     */
    public int getTotalResults() {
        int userCount = (totalUsers != null) ? totalUsers : 0;
        int helperCount = (totalHelpers != null) ? totalHelpers : 0;
        return userCount + helperCount;
    }
    
    /**
     * Check if there are any results
     */
    public boolean hasResults() {
        return getTotalResults() > 0;
    }
    
    /**
     * Check if there are user results
     */
    public boolean hasUserResults() {
        return users != null && !users.isEmpty();
    }
    
    /**
     * Check if there are helper results
     */
    public boolean hasHelperResults() {
        return helpers != null && !helpers.isEmpty();
    }
    
    /**
     * Check if pagination is available
     */
    public boolean hasPagination() {
        return totalPages != null && totalPages > 1;
    }
    
    /**
     * Check if can go to next page
     */
    public boolean canGoNext() {
        return hasNextPage != null && hasNextPage;
    }
    
    /**
     * Check if can go to previous page
     */
    public boolean canGoPrevious() {
        return hasPreviousPage != null && hasPreviousPage;
    }
    
    /**
     * Get pagination info text
     */
    public String getPaginationInfo() {
        if (currentPage == null || totalPages == null) {
            return "";
        }
        return String.format("Page %d of %d", currentPage, totalPages);
    }
    
    /**
     * Get results summary text
     */
    public String getResultsSummary() {
        int userCount = (totalUsers != null) ? totalUsers : 0;
        int helperCount = (totalHelpers != null) ? totalHelpers : 0;
        
        if (userCount == 0 && helperCount == 0) {
            return "No results found";
        }
        
        StringBuilder summary = new StringBuilder();
        if (userCount > 0) {
            summary.append(userCount).append(" user").append(userCount > 1 ? "s" : "");
        }
        
        if (helperCount > 0) {
            if (summary.length() > 0) {
                summary.append(", ");
            }
            summary.append(helperCount).append(" helper").append(helperCount > 1 ? "s" : "");
        }
        
        summary.append(" found");
        return summary.toString();
    }
}
