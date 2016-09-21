package com.nixmash.springdata.jpa.dto.addons;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.AssertTrue;

public class FlashcardImageDTO {

    private MultipartFile file;

    private long categoryId;
    private long postId = -1L;
    private String image;

    @NotEmpty
    private String content;

    @AssertTrue(message = "File must be provided")
    public boolean isFileProvided() {
        return (file != null) && ( ! file.isEmpty());
    }

    public long getCategoryId() {
        return categoryId;
    }
    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    public long getPostId() {
        return postId;
    }
    public void setPostId(long postId) {
        this.postId = postId;
    }

    public String getDescription() {
        return content;
    }
    public void setDescription(String description) {
        this.content = description;
    }

    public MultipartFile getFile() {
        return file;
    }
    public void setFile(MultipartFile file) {
        this.file = file;
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

    public FlashcardImageDTO() {
    }

    public FlashcardImageDTO(long categoryId, long postId, String image, String content) {
        this.categoryId = categoryId;
        this.postId = postId;
        this.image = image;
        this.content = content;
    }


}

