package com.example.homehelperfinder.data.model.response;

public class FavoriteHelperDto {
    private int favoriteId;
    private int userId;
    private int helperId;
    private String dateAdded;
    private FavoriteHelperDetailsDto helperInfo;

    public int getFavoriteId() {
        return favoriteId;
    }
    public void setFavoriteId(int favoriteId) {
        this.favoriteId = favoriteId;
    }
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public int getHelperId() {
        return helperId;
    }
    public void setHelperId(int helperId) {
        this.helperId = helperId;
    }
    public String getDateAdded() {
        return dateAdded;
    }
    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }
    public FavoriteHelperDetailsDto getHelperInfo() {
        return helperInfo;
    }
    public void setHelperInfo(FavoriteHelperDetailsDto helperInfo) {
        this.helperInfo = helperInfo;
    }
} 