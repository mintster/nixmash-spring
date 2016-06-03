package com.nixmash.springdata.jpa.dto;

import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.model.Post;
import com.nixmash.springdata.jpa.utils.PostUtils;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.ZonedDateTime;


public class PostDTO implements Serializable {

    private static final long serialVersionUID = 3533657789336113957L;

    private Long postId;
    private Long userId;

    @NotEmpty
    @Length(max= Post.MAX_POST_TITLE_LENGTH)
    private String postTitle;

    @NotEmpty
    private String postContent;

    private String postName;

    private String postLink;

    private ZonedDateTime postDate;

    private ZonedDateTime postModified;

    private PostType postType;

    @NotNull
    private PostDisplayType displayType;

    private Boolean isPublished = true;
    private String postSource = "NA";
    private int clickCount = 0;
    private int likesCount = 0;
    private int valueRating = 0;
    private int version = 0;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostName() {
        return postName;
    }

    public void setPostName(String postName) {
        this.postName = postName;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public ZonedDateTime getPostDate() {
        return postDate;
    }

    public void setPostDate(ZonedDateTime postDate) {
        this.postDate = postDate;
    }

    public ZonedDateTime getPostModified() {
        return postModified;
    }

    public void setPostModified(ZonedDateTime postModified) {
        this.postModified = postModified;
    }

    public PostType getPostType() {
        return postType;
    }

    public void setPostType(PostType postType) {
        this.postType = postType;
    }

    public PostDisplayType getDisplayType() {
        return displayType;
    }

    public void setDisplayType(PostDisplayType displayType) {
        this.displayType = displayType;
    }

    public Boolean getIsPublished() {
        return isPublished;
    }

    public void setIsPublished(Boolean isPublished) {
        this.isPublished = isPublished;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getPostSource() {
        return postSource;
    }

    public void setPostSource(String postSource) {
        this.postSource = postSource;
    }

    public int getClickCount() {
        return clickCount;
    }

    public void setClickCount(int clickCount) {
        this.clickCount = clickCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public int getValueRating() {
        return valueRating;
    }

    public void setValueRating(int valueRating) {
        this.valueRating = valueRating;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public boolean isNew() {
        return (this.postId == null);
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", postTitle='" + postTitle + '\'' +
                ", postName='" + postName + '\'' +
                ", postLink='" + postLink + '\'' +
                ", postDate=" + postDate +
                ", postModified=" + postModified +
                ", postType='" + postType + '\'' +
                ", displayType='" + displayType + '\'' +
                ", isPublished=" + isPublished +
                ", postContent='" + postContent + '\'' +
                ", postSource='" + postSource + '\'' +
                ", clickCount=" + clickCount +
                ", likesCount=" + likesCount +
                ", valueRating=" + valueRating +
                ", version=" + version +
                '}';
    }

    public static Builder getBuilder(Long userId, String postTitle, String postName, String postLink, String postContent, PostType postType, PostDisplayType displayType) {
        return new PostDTO.Builder(userId, postTitle, postName, postLink, postContent, postType, displayType);
    }

    public static class Builder {

        private PostDTO built;

        public Builder(Long userId, String postTitle, String postName, String postLink, String postContent, PostType postType, PostDisplayType displayType) {
            built = new PostDTO();
            built.userId = userId;
            built.postTitle = postTitle;
            built.postName = postName;
            built.postLink = postLink;
            built.postContent = postContent;
            built.postType = postType;
            built.displayType = displayType;
            built.postSource = PostUtils.getPostSource(postLink);
          }

        public PostDTO build() {
            return built;
        }
    }
}
