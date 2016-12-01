package com.nixmash.springdata.jpa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GitHubDTO {

    private long statId = -1;
    private Date statDate;

    @JsonProperty("forks_count")
    private Integer forks = 0;

    @JsonProperty("watchers_count")
    private Integer stars = 0;

    @JsonProperty("subscribers_count")
    private Integer subscribers = 0;

    private Integer followers = 0;
    private Boolean isEmpty = false;

    public Integer getFollowers() {
        return followers;
    }

    public void setFollowers(Integer followers) {
        this.followers = followers;
    }

    public Integer getForks() {
        return forks;
    }

    public void setForks(Integer forks) {
        this.forks = forks;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Integer getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(Integer subscribers) {
        this.subscribers = subscribers;
    }

    public GitHubDTO() {
    }

    public long getStatId() {
        return statId;
    }

    public void setStatId(long statId) {
        this.statId = statId;
    }

    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    public Boolean getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(Boolean empty) {
        isEmpty = empty;
    }

    @Override
    public String toString() {
        return "GitHubDTO{" +
                "statId=" + statId +
                ", forks=" + forks +
                ", stars=" + stars +
                ", subscribers=" + subscribers +
                ", followers=" + followers +
                ", statDate=" + statDate +
                '}';
    }
}
