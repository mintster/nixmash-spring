package com.nixmash.springdata.jpa.model;

import javax.persistence.*;

/**
 * Created by daveburke on 7/20/16.
 */
@Entity
@Table(name = "user_likes")
public class Like {
    private long likeId;
    private long userId;
    private long itemId;
    private int contentTypeId;

    @Id
    @Column(name = "like_id", nullable = false)
    public long getLikeId() {
        return likeId;
    }

    public void setLikeId(long likeId) {
        this.likeId = likeId;
    }

    @Basic
    @Column(name = "user_id", nullable = false)
    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "item_id", nullable = false)
    public long getItemId() {
        return itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "content_type_id", nullable = false)
    public int getContentTypeId() {
        return contentTypeId;
    }

    public void setContentTypeId(int contentTypeId) {
        this.contentTypeId = contentTypeId;
    }

    public Like() {
    }

    public Like(long userId, long itemId, int contentTypeId) {
        this.userId = userId;
        this.itemId = itemId;
        this.contentTypeId = contentTypeId;
    }

    @Override
    public String toString() {
        return "Like{" +
                "likeId=" + likeId +
                ", userId=" + userId +
                ", itemId=" + itemId +
                ", contentTypeId=" + contentTypeId +
                '}';
    }

}
