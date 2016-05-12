package com.nixmash.springdata.jpa.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class GitHubDTO {

    @JsonProperty("forks_count")
    private Integer forks;

    @JsonProperty("watchers_count")
    private Integer stars;

    @JsonProperty("subscribers_count")
    private Integer subscribers;

    private Integer followers = 0;

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
