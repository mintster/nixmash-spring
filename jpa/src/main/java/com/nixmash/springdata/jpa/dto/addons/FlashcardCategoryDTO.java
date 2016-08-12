package com.nixmash.springdata.jpa.dto.addons;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;

public class FlashcardCategoryDTO implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FlashcardCategoryDTO.class);

    private static final int MIN_LENGTH_CATEGORY = 4;
    private static final long serialVersionUID = 5924807058566818388L;
    public Long categoryId;

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    @NotEmpty
    @Length(min = MIN_LENGTH_CATEGORY)
    private String category;

    // region Getter/Setters

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // endregion


    public FlashcardCategoryDTO() {
    }

    public FlashcardCategoryDTO(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "FlashcardCategoryDTO{" +
                "categoryId=" + categoryId +
                ", category='" + category + '\'' +
                '}';
    }
}
