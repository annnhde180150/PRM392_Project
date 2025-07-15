package com.example.homehelperfinder.data.model;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Model for pagination information
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Pagination {
    @SerializedName("page")
    private int page;
    
    @SerializedName("pageSize")
    private int pageSize;
    
    @SerializedName("totalCount")
    private int totalCount;
    
    @SerializedName("totalPages")
    private int totalPages;
    
    @SerializedName("hasNext")
    private boolean hasNext;
    
    @SerializedName("hasPrevious")
    private boolean hasPrevious;
    
    /**
     * Check if this is the first page
     */
    public boolean isFirstPage() {
        return page <= 1;
    }
    
    /**
     * Check if this is the last page
     */
    public boolean isLastPage() {
        return !hasNext;
    }
    
    /**
     * Get the next page number
     */
    public int getNextPage() {
        return hasNext ? page + 1 : page;
    }
    
    /**
     * Get the previous page number
     */
    public int getPreviousPage() {
        return hasPrevious ? page - 1 : page;
    }
}
