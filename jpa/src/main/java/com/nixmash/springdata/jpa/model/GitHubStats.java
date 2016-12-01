package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.sql.Date;

/**
 * Created by daveburke on 12/1/16.
 */
@Entity
@Table(name = "github_stats")
public class GitHubStats {
    private long statId;
    private Date statDate;
    private int followers = 0;
    private int subscribers = 0;
    private int stars = 0;
    private int forks = 0;

    @Id
    @Column(name = "stat_id", unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public long getStatId() {
        return statId;
    }

    public void setStatId(long statId) {
        this.statId = statId;
    }

    @Basic
    @Column(name = "stat_date", nullable = false)
    public Date getStatDate() {
        return statDate;
    }

    public void setStatDate(Date statDate) {
        this.statDate = statDate;
    }

    @Basic
    @Column(name = "followers", nullable = false)
    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    @Basic
    @Column(name = "subscribers", nullable = false)
    public int getSubscribers() {
        return subscribers;
    }

    public void setSubscribers(int subscribers) {
        this.subscribers = subscribers;
    }

    @Basic
    @Column(name = "stars", nullable = false)
    public int getStars() {
        return stars;
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

    @Basic
    @Column(name = "forks", nullable = false)
    public int getForks() {
        return forks;
    }

    public void setForks(int forks) {
        this.forks = forks;
    }

    @Override
    public String toString() {
        return "GitHubStats{" +
                "statId=" + statId +
                ", statDate=" + statDate +
                ", followers=" + followers +
                ", subscribers=" + subscribers +
                ", stars=" + stars +
                ", forks=" + forks +
                '}';
    }
}
