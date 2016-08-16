package com.nixmash.springdata.jpa.dto.addons;

import java.time.ZonedDateTime;

public class FlashcardDTO {
    private long slideId;

    private long categoryId;
    private long postId;
    private String image;
    private String content;
    public ZonedDateTime datetimeCreated;
    public String categoryName;
    public String imageUrl;
    public String thumbnailUrl;
    public String postTitle;

    public long getSlideId() {
        return slideId;
    }
    public void setSlideId(long slideId) {
        this.slideId = slideId;
    }

    public long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public ZonedDateTime getDatetimeCreated() {
        return datetimeCreated;
    }
    public void setDatetimeCreated(ZonedDateTime dateCreated) {
        this.datetimeCreated = dateCreated;
    }

    public long getPostId() {
        return postId;
    }
    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getCategoryName() {
        return categoryName;
    }
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getPostTitle() {
        return postTitle;
    }
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public FlashcardDTO() {
    }

    public FlashcardDTO(long categoryId, long postId, String image, String content) {
        this.categoryId = categoryId;
        this.postId = postId;
        this.image = image;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "slideId=" + slideId +
                ", categoryId=" + categoryId +
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", category ='" + categoryName + '\'' +
                ", dateCreated=" + datetimeCreated +
                '}';
    }
}
