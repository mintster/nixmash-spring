package com.nixmash.springdata.jpa.model;


public class UserConnection {

    private final String userId;

    private final String providerId;
    private final String providerUserId;
    private final int rank;
    private final String displayName;
    private final String profileUrl;
    private final String imageUrl;
    private final String accessToken;
    private final String secret;
    private final String refreshToken;
    private final Long expireTime;

    public UserConnection(String userId, String providerId, String providerUserId, int rank, String displayName, String profileUrl, String imageUrl, String accessToken, String secret, String refreshToken, Long expireTime) {
        this.userId = userId;
        this.providerId = providerId;
        this.providerUserId = providerUserId;
        this.rank = rank;
        this.displayName = displayName;
        this.profileUrl = profileUrl;
        this.imageUrl = imageUrl;
        this.accessToken = accessToken;
        this.secret = secret;
        this.refreshToken = refreshToken;
        this.expireTime = expireTime;
    }

    public String toString() {
        return
            "userId = " + userId +
            ", providerId = " + providerId +
            ", providerUserId = " + providerUserId +
            ", rank = " + rank +
            ", displayName = " + displayName +
            ", profileUrl = " + profileUrl +
            ", imageUrl = " + imageUrl +
            ", accessToken = " + accessToken +
            ", secret = " + secret +
            ", refreshToken = " + refreshToken +
            ", expireTime = " + expireTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderUserId() {
        return providerUserId;
    }

    public int getRank() {
        return rank;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getSecret() {
        return secret;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public Long getExpireTime() {
        return expireTime;
    }
}