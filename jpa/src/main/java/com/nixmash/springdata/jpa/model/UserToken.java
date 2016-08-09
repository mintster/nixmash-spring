package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by daveburke on 8/7/16.
 */
@Entity
@Table(name = "user_tokens")
public class UserToken {
    private long tokenId;
    private User user;
    private String token;
    private Timestamp tokenExpiration;

    private static final int EXPIRATION = 60 * 24;

    @Id
    @Column(name = "token_id", nullable = false)
    public long getTokenId() {
        return tokenId;
    }

    public void setTokenId(long tokenId) {
        this.tokenId = tokenId;
    }

    @OneToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Column(name = "token", nullable = true, length = 255)
    public String getToken() {
        return token;
    }

    public void setToken(String userToken) {
        this.token = userToken;
    }

    @Basic
    @Column(name = "token_expiration", nullable = false)
    public Timestamp getTokenExpiration() {
        return tokenExpiration;
    }

    public void setTokenExpiration(Timestamp tokenExpiration) {
        this.tokenExpiration = tokenExpiration;
    }

    public UserToken() {
        super();
    }

    public UserToken(final String token) {
        super();
        this.token = token;
        this.tokenExpiration = calculateExpiryDate(EXPIRATION);
    }

    public UserToken(final String token, final User user) {
        super();

        this.token = token;
        this.user = user;
        this.tokenExpiration = calculateExpiryDate(EXPIRATION);
    }

    private Timestamp calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Timestamp(cal.getTime().getTime());
    }

    public void updateToken(final String token) {
        this.token = token;
        this.tokenExpiration = calculateExpiryDate(EXPIRATION);
    }

    @Override
    public String toString() {
        return "UserToken{" +
                "tokenId=" + tokenId +
                ", user=" + user.getUsername() +
                ", userToken='" + token + '\'' +
                ", tokenExpiration=" + tokenExpiration +
                '}';
    }
}
