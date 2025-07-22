package com.example.homehelperfinder.data.model.request;

import com.google.gson.annotations.SerializedName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Request model for searching users and helpers for chat
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchChatRequest {
    
    @SerializedName("searchType")
    private String searchType; // "user", "helper", "all"
    
    @SerializedName("searchTerm")
    private String searchTerm; // Search keyword for name, email, etc.
    
    @SerializedName("email")
    private String email; // Specific email to search
    
    @SerializedName("isActive")
    private Boolean isActive; // Filter by active status
    
    @SerializedName("pageSize")
    private Integer pageSize; // Number of results per page
    
    @SerializedName("pageNumber")
    private Integer pageNumber; // Current page number
    
    @SerializedName("availabilityStatus")
    private String availabilityStatus; // Helper availability status
    
    @SerializedName("minimumRating")
    private Double minimumRating; // Minimum rating for helpers
    
    /**
     * Constructor for basic search
     */
    public SearchChatRequest(String searchTerm, Integer pageNumber, Integer pageSize) {
        this.searchTerm = searchTerm;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.searchType = "all";
        this.isActive = true;
    }
    
    /**
     * Constructor for user-only search
     */
    public static SearchChatRequest forUsers(String searchTerm, Integer pageNumber, Integer pageSize) {
        SearchChatRequest request = new SearchChatRequest();
        request.setSearchType("user");
        request.setSearchTerm(searchTerm);
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setIsActive(true);
        return request;
    }
    
    /**
     * Constructor for helper-only search
     */
    public static SearchChatRequest forHelpers(String searchTerm, Integer pageNumber, Integer pageSize, Double minimumRating) {
        SearchChatRequest request = new SearchChatRequest();
        request.setSearchType("helper");
        request.setSearchTerm(searchTerm);
        request.setPageNumber(pageNumber);
        request.setPageSize(pageSize);
        request.setIsActive(true);
        request.setMinimumRating(minimumRating);
        return request;
    }
}
