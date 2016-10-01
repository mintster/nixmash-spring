package com.nixmash.springdata.jpa.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "tags")
@NamedNativeQueries({
        @NamedNativeQuery(
                name = "getTagCloud",
                query = "select count(*) as `tagCount`, t.tag_value, t.tag_id from tags t " +
                        " inner join post_tag_ids pt on t.tag_id = pt.tag_id " +
                        " inner join posts p on pt.post_id = p.post_id " +
                        " where p.is_published = true " +
                        "group by t.tag_value order by t.tag_value;",
                resultClass = Tag.class)
})
public class Tag implements Serializable {

    private static final long serialVersionUID = -5531381747015731447L;

    private long tagId;
    private String tagValue;
    private Set<Post> posts;
    private int tagCount = 0;

    public Tag() {
    }

    public Tag(String tagValue) {
        this.tagValue = tagValue;
    }

    public Tag(Long tagId, String tagValue) {
        this.tagId = tagId;
        this.tagValue = tagValue;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "tag_id", nullable = false)
    public long getTagId() {
        return tagId;
    }

    public void setTagId(long tagId) {
        this.tagId = tagId;
    }

    @Basic
    @Column(name = "tag_value", nullable = false, length = 50)
    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    @ManyToMany(mappedBy = "tags")
    public Set<Post> getPosts() {
        return posts;
    }

    public void setPosts(Set<Post> posts) {
        this.posts = posts;
    }

    @Transient
    public int getTagCount() {
        return tagCount;
    }

    public void setTagCount(int tagCount) {
        this.tagCount = tagCount;
    }

    @Override
    public String toString() {
        return getTagValue();
    }

    public void update(final String tagValue) {
        this.tagValue = tagValue;
    }

    public static Builder getBuilder(Long tagId, String tagValue) {
        return new Builder(tagId, tagValue);
    }

    public static class Builder {

        private Tag built;

        public Builder(Long tagId, String tagValue) {
            built = new Tag();
            built.tagId = tagId;
            built.tagValue = tagValue;
        }

        public Tag build() {
            return built;
        }
    }

}
