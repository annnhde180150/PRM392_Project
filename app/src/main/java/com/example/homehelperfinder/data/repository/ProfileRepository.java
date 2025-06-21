package com.example.homehelperfinder.data.repository;

import com.example.homehelperfinder.data.model.ProfileModel;
import com.example.homehelperfinder.data.remote.ProfileApiService;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Repository class for profile management operations
 * Acts as a single source of truth for profile data
 */
public class ProfileRepository {
    private final ProfileApiService apiService;

    public ProfileRepository() {
        this.apiService = new ProfileApiService();
    }

    /**
     * Get all banned profiles
     * @return CompletableFuture containing list of banned profiles
     */
    public CompletableFuture<List<ProfileModel>> getBannedProfiles() {
        return apiService.getBannedProfiles();
    }

    /**
     * Get all active profiles
     * @return CompletableFuture containing list of active profiles
     */
    public CompletableFuture<List<ProfileModel>> getActiveProfiles() {
        return apiService.getActiveProfiles();
    }
} 