package com.nixmash.springdata.jpa.model.addons;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "flashcard_categories")
public class FlashcardCategory implements Serializable {


    private static final Logger logger = LoggerFactory.getLogger(FlashcardCategory.class);

    private static final long serialVersionUID = 4542457686252608175L;
    private static final int MIN_LENGTH_CATEGORY = 4;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    public Long categoryId;

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    @Column(unique = true)
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


    public FlashcardCategory() {
    }

    public FlashcardCategory(String category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "FlashcardCategory{" +
                "categoryId=" + categoryId +
                ", category='" + category + '\'' +
                '}';
    }

}
