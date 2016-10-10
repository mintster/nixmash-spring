package com.nixmash.springdata.jpa.model;

import com.nixmash.springdata.jpa.enums.PostDisplayType;
import com.nixmash.springdata.jpa.enums.PostType;
import com.nixmash.springdata.jpa.utils.PostUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import static javax.persistence.AccessType.FIELD;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
@Access(value = FIELD)
@NamedQueries({
        @NamedQuery(name = "Post.getByPostIds",
                query = "SELECT p FROM Post p " +
                        "WHERE p.postId IN :postIds ORDER BY p.postDate DESC"),
})
public class Post implements Serializable {

    private static final long serialVersionUID = 3533657789336113957L;

    public static final int MAX_POST_TITLE_LENGTH = 200;
    public static final int MAX_POST_NAME_LENGTH = 200;
    public static final int MIN_POST_CONTENT_LENGTH = 20;

    // region properties

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "post_id", nullable = false)
    private Long postId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "post_title", nullable = false, length = MAX_POST_TITLE_LENGTH)
    private String postTitle;

    @Column(name = "post_name", nullable = false, length = MAX_POST_NAME_LENGTH)
    private String postName;

    @Column(name = "post_link")
    private String postLink;

    @Column(name = "post_image", length = MAX_POST_NAME_LENGTH)
    private String postImage;

    private ZonedDateTime postDate;

    private ZonedDateTime postModified;

    @Column(name = "post_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column(name = "display_type", nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private PostDisplayType displayType;

    @Column(name = "is_published", nullable = false)
    private Boolean isPublished = true;

    @Column(name = "post_content", nullable = false, columnDefinition = "TEXT")
    private String postContent;

    @Column(name = "post_source", length = 50)
    private String postSource = "NA";

    @Column(name = "click_count", nullable = false)
    private int clickCount = 0;

    @Column(name = "likes_count", nullable = false)
    private int likesCount = 0;

    @Column(name = "value_rating", nullable = false)
    private int valueRating = 0;

    @Version
    @Column(name = "version", nullable = false, insertable = true, updatable = true)
    private int version = 0;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "post_tag_ids",
            joinColumns = @JoinColumn(name = "post_id",
                    referencedColumnName = "post_id",
                    nullable = false),
            inverseJoinColumns = @JoinColumn(name = "tag_id",
                    referencedColumnName = "tag_id",
                    nullable = false))
    public Set<Tag> tags;

    @Transient
    public List<PostImage> postImages;

    @Transient
    public PostImage singleImage;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", insertable = false, updatable = false)
    public User author;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    // endregion

    // region Transient properties

    @Transient
    public boolean isOwner = false;

    // endregion

    // region method properties

    public boolean isNew() {
        return (this.postId == null);
    }

    public String getAuthorFullname() {
        return author.getFirstName() + " " +  author.getLastName();
    }


    // endregion

    //region Getter Setters

    public PostImage getSingleImage() {
        return singleImage;
    }
    public void setSingleImage(PostImage singleImage) {
        this.singleImage = singleImage;
    }

    public List<PostImage> getPostImages() {
        return postImages;
    }
    public void setPostImages(List<PostImage> postImages) {
        this.postImages = postImages;
    }

    public boolean getIsOwner() {
        return isOwner;
    }
    public void setIsOwner(boolean owner) {
        isOwner = owner;
    }

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

    @Column(name = "post_date", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @CreatedDate
    public ZonedDateTime getPostDate() {
        return postDate;
    }
    public void setPostDate(ZonedDateTime postDate) {
        this.postDate = postDate;
    }

    @Column(name = "post_modified", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.threeten.PersistentLocalDateTime")
    @LastModifiedDate
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

    public String getPostImage() {
        return postImage;
    }

    public void setPostImage(String postImage) {
        this.postImage = postImage;
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

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    //endregion

    public void update(String postTitle, String postContent, Boolean isPublished, PostDisplayType displayType) {
        this.postTitle = postTitle;
        this.postContent = postContent;
        this.isPublished = isPublished;
        this.displayType = displayType;
    }

    public void updateLikes(int likeIncrement) {
        this.likesCount = this.likesCount + likeIncrement;
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
        return new Post.Builder(userId, postTitle, postName, postLink, postContent, postType, displayType);
    }

    public static class Builder {

        private Post built;

        public Builder(Long userId, String postTitle, String postName, String postLink, String postContent, PostType postType, PostDisplayType displayType) {
            built = new Post();
            built.userId = userId;
            built.postTitle = postTitle;
            built.postName = postName;
            built.postLink = postLink;
            built.postContent = postContent;
            built.postType = postType;
            built.displayType = displayType;
            built.postSource = PostUtils.createPostSource(postLink);
        }

        public Builder isPublished(Boolean isPublished) {
            built.setIsPublished(isPublished);
            return this;
        }

        public Builder postSource(String postSource) {
            built.postSource = postSource;
            return this;
        }

        public Builder postId(long postId) {
            built.postId = postId;
            return this;
        }

        public Builder postImage(String postImage) {
            if (StringUtils.isEmpty(postImage))
                postImage = null;
            built.postImage = postImage;
            return this;
        }

        public Builder tags(Set<Tag> tags) {
            built.tags= tags;
            return this;
        }

        public Post build() {
            return built;
        }
    }
}
