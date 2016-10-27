package com.nixmash.springdata.jpa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GitHubDTO {

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

    public Boolean getIsEmpty() {
        return isEmpty;
    }

    public void setIsEmpty(Boolean empty) {
        isEmpty = empty;
    }

    @Override
    public String toString() {
        return "GitHubDTO{" +
                "forks=" + forks +
                ", stars=" + stars +
                ", subscribers=" + subscribers +
                ", followers=" + followers +
                '}';
    }
}
