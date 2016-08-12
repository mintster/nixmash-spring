package com.nixmash.springdata.jpa.model.addons;

import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.ZonedDateTime;

import static javax.persistence.AccessType.PROPERTY;

/**
 * Created by daveburke on 8/7/16.
 */
@Entity
@Table(name = "flashcard_slides")
@Access(PROPERTY)
@EntityListeners(AuditingEntityListener.class)
public class Flashcard {
    private long slideId;

    private long categoryId;
    private String image;
    private String content;

    @Id
    @Column(name = "slide_id", nullable = false)
    public long getSlideId() {
        return slideId;
    }

    public void setSlideId(long slideId) {
        this.slideId = slideId;
    }


    @Basic
    @Column(name = "category_id", nullable = false)
    public long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(long categoryId) {
        this.categoryId = categoryId;
    }

    @Basic
    @Column(name = "slide_image")
    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    @Basic
    @Column(name = "slide_content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    @Column(name = "datetime_created", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @CreatedDate
    public ZonedDateTime datetimeCreated;

    public ZonedDateTime getDatetimeCreated() {
        return datetimeCreated;
    }

    public void setDatetimeCreated(ZonedDateTime dateCreated) {
        this.datetimeCreated = dateCreated;
    }

    public Flashcard() {
    }

    public Flashcard(long categoryId, String image, String content) {
        this.categoryId = categoryId;
        this.image = image;
        this.content = content;
    }

    @Override
    public String toString() {
        return "Flashcard{" +
                "slideId=" + slideId +
                ", categoryId=" + categoryId+
                ", image='" + image + '\'' +
                ", content='" + content + '\'' +
                ", dateCreated=" + datetimeCreated +
                '}';
    }
}
