package com.nixmash.springdata.jpa.model.addons;

import com.nixmash.springdata.jpa.model.Post;
import org.hibernate.annotations.Type;
import org.jadira.usertype.dateandtime.threeten.PersistentZonedDateTime;
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
@SqlResultSetMapping(name = "FlashcardsWithCategory",
        classes = {
                @ConstructorResult(
                        targetClass = Flashcard.class,
                        columns = {
                                @ColumnResult(name = "slide_id", type = Long.class),
                                @ColumnResult(name = "category_id", type = Long.class),
                                @ColumnResult(name = "datetime_created",
                                                                                             type= PersistentZonedDateTime.class),
                                @ColumnResult(name = "slide_content"),
                                @ColumnResult(name = "slide_image"),
                                @ColumnResult(name = "category"),
                                @ColumnResult(name = "post_title"),
                                @ColumnResult(name = "post_id", type = Long.class)
                        }
                )
        }
)
public class Flashcard {
    private long slideId;

    private long categoryId;
    private String image;
    private String content;

    public Flashcard(long slideId, long categoryId, ZonedDateTime datetimeCreated,
                     String content, String image, String categoryName, String postTitle, long postId) {
        this.slideId = slideId;
        this.categoryId = categoryId;
        this.image = image;
        this.content = content;
        this.datetimeCreated = datetimeCreated;
        this.categoryName = categoryName;
        this.postTitle = postTitle;
        this.postId = postId;
    }

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

    public String categoryName;

    @Transient
    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String postTitle;

    @Transient
    public String getPostTitle() {
        return postTitle;
    }
    public void setPostTitle(String postTitle) {
        this.postTitle = postTitle;
    }

    public FlashcardCategory category;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id", referencedColumnName = "category_id", insertable = false, updatable = false)
    public FlashcardCategory getCategory() {
        return category;
    }

    public void setCategory(FlashcardCategory category) {
        this.category = category;
    }

    public Post post;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", referencedColumnName = "post_id", insertable = false)
    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String imageUrl;
    public String thumbnailUrl;

    @Transient
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Transient
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public long postId;

    @Transient
    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }


    public Flashcard() {
    }

    public Flashcard(long categoryId, String image, String content, Post post) {
        this.categoryId = categoryId;
        this.post = post;
        this.image = image;
        this.content = content;
        this.postId = post.getPostId();
    }

    public void update(long categoryId, String content, Post post) {
        this.categoryId = categoryId;
        this.content = content;
        this.post = post;
        this.postId = post.getPostId();
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
