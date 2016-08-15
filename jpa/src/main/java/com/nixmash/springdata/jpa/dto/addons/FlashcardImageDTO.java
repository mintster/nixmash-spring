package com.nixmash.springdata.jpa.dto.addons;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.AssertTrue;

public class FlashcardImageDTO {

    private MultipartFile file;

    private long categoryId = -1L;

    @NotEmpty
    private String description;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}

