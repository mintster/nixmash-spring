package com.nixmash.springdata.jpa.model;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "post_images")
@JsonIgnoreProperties({"id", "postId", "thumbnailFilename", "newFilename", "contentType", "dateCreated", "lastUpdated"})
public class PostImage implements Serializable {

    private static final long serialVersionUID = 5143474574941155184L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;

    @Column(name = "post_id")
    private Long postId;

    @Column(name = "image_name")
    private String name;

    @Column(name = "thumbnail_filename")
    private String thumbnailFilename;

    @Column(name = "filename")
    private String newFilename;

    @Column(name = "content_type")
    private String contentType;
    private Long size;

    @Column(name = "thumbnail_size")
    private Long thumbnailSize;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "datetime_created")
    private Date dateCreated;

    @Transient
    private String url;
    @Transient
    private String thumbnailUrl;
    @Transient
    private String deleteUrl;
    @Transient
    private String deleteType;

    public PostImage() {
    }

    // region Setters / Getters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnailFilename() {
        return thumbnailFilename;
    }

    public void setThumbnailFilename(String thumbnailFilename) {
        this.thumbnailFilename = thumbnailFilename;
    }

    public String getNewFilename() {
        return newFilename;
    }

    public void setNewFilename(String newFilename) {
        this.newFilename = newFilename;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getThumbnailSize() {
        return thumbnailSize;
    }

    public void setThumbnailSize(Long thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDeleteUrl() {
        return deleteUrl;
    }

    public void setDeleteUrl(String deleteUrl) {
        this.deleteUrl = deleteUrl;
    }

    public String getDeleteType() {
        return deleteType;
    }

    public void setDeleteType(String deleteType) {
        this.deleteType = deleteType;
    }

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    // endregion


    @Override
    public String toString() {
        return "Image{" + "name=" + name + ", thumbnailFilename=" + thumbnailFilename + ", newFilename=" + newFilename + ", contentType=" + contentType + ", url=" + url + ", thumbnailUrl=" + thumbnailUrl + ", deleteUrl=" + deleteUrl + ", deleteType=" + deleteType + '}';
    }

    public static Builder getBuilder(Long postId, String name, String thumbnailFilename, String newFilename, String contentType, Long size, Long thumbnailSize, Date dateCreated, String url, String thumbnailUrl, String deleteUrl, String deleteType) {
        return new PostImage.Builder(postId, name, thumbnailFilename, newFilename, contentType, size, thumbnailSize, dateCreated, url, thumbnailUrl, deleteUrl, deleteType);
    }

    public static class Builder {
        private PostImage built;

        public Builder(Long postId, String name, String thumbnailFilename, String newFilename, String contentType, Long size, Long thumbnailSize, Date dateCreated, String url, String thumbnailUrl, String deleteUrl, String deleteType) {
            built.postId = postId;
            built.name = name;
            built.thumbnailFilename = thumbnailFilename;
            built.newFilename = newFilename;
            built.contentType = contentType;
            built.size = size;
            built.thumbnailSize = thumbnailSize;
            built.dateCreated = dateCreated;
            built.url = url;
            built.thumbnailUrl = thumbnailUrl;
            built.deleteUrl = deleteUrl;
            built.deleteType = deleteType;
        }

        public PostImage build() {
            return built;
        }
    }
}
