package com.nixmash.springdata.mvc.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by daveburke on 9/16/16.
 */


public class JsonPostDTO  implements Serializable {


    private static final long serialVersionUID = 4872905888480797482L;
    private Long postId;
    private Set<String> tags = new HashSet<>();
    private String postTitle;
    private String postContent;
    private Boolean isPublished;
    private String displayType;

    public JsonPostDTO(Long postId, Set<String> tags, String postTitle, String postContent, Boolean isPublished, String displayType) {
        this.postId = postId;
        this.tags = tags;
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.isPublished = isPublished;
        this.displayType = displayType;
    }

    public JsonPostDTO() {

    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public String getDisplayType() {
        return displayType;
    }

    public void setDisplayType(String displayType) {
        this.displayType = displayType;
    }
}